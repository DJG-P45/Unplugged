package com.example.unplugged.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.datasource.PseudoEskomSePushNetworkApi;
import com.example.unplugged.data.datasource.ObservedAreaDao;
import com.example.unplugged.data.datasource.UnpluggedDatabase;
import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.OutageDto;
import com.example.unplugged.data.dto.StageDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.entity.ObservedAreaEntity;
import com.example.unplugged.data.other.ErrorCategory;
import com.example.unplugged.ui.viewmodel.Consumable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class LoadSheddingRepository implements ILoadSheddingRepository {

    private final ObservedAreaDao observedAreaDao;
    private final PseudoEskomSePushNetworkApi pseudoEskomSePushNetworkApi;
    private final ObjectMapper mapper;
    private final MutableLiveData<Consumable<ErrorCategory>> errorFeed;

    public LoadSheddingRepository(Application application) {
        this.pseudoEskomSePushNetworkApi = new PseudoEskomSePushNetworkApi();
        UnpluggedDatabase db = UnpluggedDatabase.getDatabase(application);
        observedAreaDao = db.observedAreaDao();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        errorFeed = new MutableLiveData<>();
    }

    @Override
    public LiveData<Consumable<ErrorCategory>> getErrorFeed() {
        return errorFeed;
    }

    @Override
    public void observeArea(String id) {
        runAsync(() -> observedAreaDao.insert(new ObservedAreaEntity(id)));
    }

    @Override
    public void removeObservedArea(String id) {
        runAsync(() -> observedAreaDao.delete(id));
    }

    @Override
    public LiveData<StatusDto> getStatus() {
        MutableLiveData<StatusDto> mutableLiveData = new MutableLiveData<>();

        runAsync(() -> pseudoEskomSePushNetworkApi.getStatus(getErrorConsumer(), json -> {
            try {
                mutableLiveData.postValue(mapper.readValue(json, StatusDto.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                error(ErrorCategory.SYSTEM);
            }
        }));

        return mutableLiveData;
    }

    @Override
    public LiveData<List<FoundAreaDto>> findAreas(String searchText) {
        MutableLiveData<List<FoundAreaDto>> mutableLiveData = new MutableLiveData<>();

        runAsync(() -> pseudoEskomSePushNetworkApi.findAreas(searchText, getErrorConsumer(), json -> {
            FoundAreaDto[] foundAreaArray = new FoundAreaDto[0];
            try {
                foundAreaArray = mapper.readValue(json, FoundAreaDto[].class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                error(ErrorCategory.SYSTEM);
            }
            mutableLiveData.postValue(new ArrayList<>(Arrays.asList(foundAreaArray)));
        }));

        return mutableLiveData;
    }

    @Override
    public LiveData<List<AreaDto>> getObservedAreas() {
        MutableLiveData<List<AreaDto>> mutableLiveData = new MutableLiveData<>();

        observedAreaDao.getAllObservedAreas().observeForever(observedAreas -> {
            List<AreaDto> areas = new ArrayList<>();

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
        MutableLiveData<AreaDto> mutableLiveData = new MutableLiveData<>();

        runAsync(() -> pseudoEskomSePushNetworkApi.getAreaInfo(areaId, getErrorConsumer(), json -> {
            AreaDto areaDto = null;
            try {
                areaDto = mapper.readValue(json, AreaDto.class);
                areaDto.setId(areaId);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                error(ErrorCategory.SYSTEM);
            }
            mutableLiveData.postValue(areaDto);
        }));

        return mutableLiveData;
    }

    @Override
    public LiveData<DayScheduleDto> getDaySchedule(String areaId, LocalDate date) {
        MutableLiveData<DayScheduleDto> mutableLiveData = new MutableLiveData<>();

        getStatus().observeForever(statusDto -> getArea(areaId).observeForever(areaDto -> {
            DayScheduleDto schedule = new DayScheduleDto();
            schedule.setAreaName(areaDto.getInfo().getName());
            schedule.setDate(date.getDayOfMonth() + " " + date.getMonth().name());
            schedule.setDowntime("No downtime");
            schedule.setOutages(new ArrayList<>());

            for (DayDto dayDto : areaDto.getSchedule().getDays()) {
                if (dayDto.getDate().isEqual(date)) {
                    StageDto stageDto = dayDto.getStages().get(statusDto.getStage());
                    schedule.setOutages(stageDto.getOutages());
                    schedule.setDowntime(totalDowntime(stageDto.getOutages()));
                }
            }
            mutableLiveData.postValue(schedule);
        }));

        return  mutableLiveData;
    }

    private static void runAsync(Runnable runnable) {
        Executors.newSingleThreadExecutor().execute(runnable);
    }

    private synchronized void error(ErrorCategory errorCategory) {
        errorFeed.postValue(new Consumable<>(errorCategory));
    }

    private Consumer<String> getErrorConsumer() {
        return s -> error(ErrorCategory.NETWORK);
    }

    private static String totalDowntime(List<OutageDto> outages) {

        if (outages.size() == 0) return "No downtime";

        int totalTimeInMinutes = 0;

        for (OutageDto outageDto: outages) {
            totalTimeInMinutes += outageDto.getDurationInMinutes();
        }

        int hours = totalTimeInMinutes / 60;
        int minutes = totalTimeInMinutes % 60;

        String result = "Downtime:";

        if (hours > 1) result += " " + hours + " hours";
        if (hours == 1) result += " " + hours + " hour";
        if (minutes > 1) result += " " + minutes + " minutes";
        if (minutes == 1) result += " " + minutes + " minute";

        return   result;
    }
}
