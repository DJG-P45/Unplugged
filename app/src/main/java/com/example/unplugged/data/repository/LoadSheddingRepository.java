package com.example.unplugged.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.datasource.EskomSePushNetworkApi;
import com.example.unplugged.data.datasource.ObservedAreaDao;
import com.example.unplugged.data.datasource.UnpluggedDatabase;
import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.StageDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.entity.ObservedAreaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoadSheddingRepository implements ILoadSheddingRepository {

    private ObservedAreaDao observedAreaDao;
    private EskomSePushNetworkApi eskomSePushNetworkApi;
    private ObjectMapper mapper;

    public LoadSheddingRepository(Application application) {
        this.eskomSePushNetworkApi = new EskomSePushNetworkApi();
        UnpluggedDatabase db = UnpluggedDatabase.getDatabase(application);
        observedAreaDao = db.observedAreaDao();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public LiveData<StatusDto> getStatus() {
        Executor executor = Executors.newSingleThreadExecutor();
        StatusDto statusDto = new StatusDto();
        MutableLiveData<StatusDto> mutableLiveData = new MutableLiveData<>();

        executor.execute(() -> eskomSePushNetworkApi.getStatus(jsonObject -> {
            try {
                JSONObject status = jsonObject.getJSONObject("status");
                JSONObject eskom = status.getJSONObject("eskom");
                statusDto.setStage(eskom.getInt("stage"));
                statusDto.setUpdated(ZonedDateTime.parse(eskom.getString("stage_updated")));
                mutableLiveData.postValue(statusDto);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        return mutableLiveData;
    }

    @Override
    public LiveData<List<FoundAreaDto>> findAreas(String searchText) {
        Executor executor = Executors.newSingleThreadExecutor();
        List<FoundAreaDto> foundAreas = new ArrayList<>();
        MutableLiveData<List<FoundAreaDto>> mutableLiveData = new MutableLiveData<>();

        executor.execute(() -> eskomSePushNetworkApi.findAreas("...", jsonObject -> {
            try {
                JSONArray areas = jsonObject.getJSONArray("areas");

                for (int i = 0; i < areas.length(); i++) {
                    JSONObject area = areas.getJSONObject(i);
                    FoundAreaDto foundAreaDto = new FoundAreaDto();
                    foundAreaDto.setId(area.getString("id"));
                    foundAreaDto.setName(area.getString("name"));
                    foundAreaDto.setRegion(area.getString("region"));
                    foundAreas.add(foundAreaDto);
                }

                mutableLiveData.postValue(foundAreas);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        return mutableLiveData;
    }

    @Override
    public void observeArea(String id) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> observedAreaDao.insert(new ObservedAreaEntity(id)));
    }

    @Override
    public void removeObservedArea(String id) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> observedAreaDao.delete(id));
    }

    @Override
    public LiveData<List<AreaDto>> getObservedAreas() {
        List<AreaDto> areas = new ArrayList<>();
        MutableLiveData<List<AreaDto>> mutableLiveData = new MutableLiveData<>();

        observedAreaDao.getAllObservedAreas().observeForever(observedAreas -> {
            for (ObservedAreaEntity entity : observedAreas) {
                getArea(entity.getId()).observeForever(areaDto -> {
                    areas.add(areaDto);
                    mutableLiveData.postValue(areas);
                });
            }
        });

        return mutableLiveData;
    }


    @Override
    public LiveData<AreaDto> getArea(String areaId) {
        Executor executor = Executors.newSingleThreadExecutor();
        MutableLiveData<AreaDto> mutableLiveData = new MutableLiveData<>();

        executor.execute(() -> eskomSePushNetworkApi.getAreaInfo(areaId, jsonObject -> {
            try {
                AreaDto areaDto = mapper.readValue(jsonObject.toString(), AreaDto.class);
                mutableLiveData.postValue(areaDto);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }));

        return mutableLiveData;
    }

    @Override
    public LiveData<DayScheduleDto> getDaySchedule(AreaDto areaDto, LocalDate date) {
        MutableLiveData<DayScheduleDto> mutableLiveData = new MutableLiveData<>();

        getStatus().observeForever(statusDto -> {
            DayScheduleDto schedule = new DayScheduleDto();
            schedule.setAreaName(areaDto.getInfo().getName());
            schedule.setDate(date.getDayOfMonth() + " " + date.getMonth().name());
            schedule.setDowntime("");
            schedule.setOutages(new ArrayList<>());

            for (DayDto dayDto : areaDto.getSchedule().getDays()) {
                if (dayDto.getDate().isEqual(date)) {
                    StageDto stageDto = dayDto.getStages().get(statusDto.getStage());
                    schedule.setOutages(stageDto.getOutages());
                }
            }
            mutableLiveData.postValue(schedule);
        });

        return  mutableLiveData;
    }
}
