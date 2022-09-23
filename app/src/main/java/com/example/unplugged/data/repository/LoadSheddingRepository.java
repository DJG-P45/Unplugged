package com.example.unplugged.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.datasource.EskomSePushNetworkApi;
import com.example.unplugged.data.datasource.UnpluggedDatabase;
import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.dto.EventDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.StatusDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoadSheddingRepository implements ILoadSheddingRepository {

    private UnpluggedDatabase unpluggedDatabase;
    private EskomSePushNetworkApi eskomSePushNetworkApi;

    public LoadSheddingRepository() {
        this.eskomSePushNetworkApi = new EskomSePushNetworkApi();
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
                statusDto.setStage("Stage " + eskom.getString("stage"));
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

    }

    @Override
    public void removeObservedArea(String id) {

    }

    // TODO: Have one endpoint for getting areas. Restructure DTOs to match JSON response structure.
    // Allows me to avoid implementing caching and making two separate api calls

    @Override
    public LiveData<List<AreaDto>> getObservedAreas() {
        Executor executor = Executors.newSingleThreadExecutor();
        List<AreaDto> areas = new ArrayList<>();
        MutableLiveData<List<AreaDto>> mutableLiveData = new MutableLiveData<>();

        executor.execute(() -> eskomSePushNetworkApi.getAreaInfo("id-here", jsonObject -> {
            try {
                JSONObject info = jsonObject.getJSONObject("info");
                AreaDto areaDto = new AreaDto();
                areaDto.setName(info.getString("name"));
                areaDto.setRegion(info.getString("region"));

                JSONArray events = jsonObject.getJSONArray("events");
                JSONObject firstEvent = (JSONObject) events.get(0);
                areaDto.setEvent(firstEvent.getString("note"));

                areas.add(areaDto);
                mutableLiveData.postValue(areas);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        return mutableLiveData;
    }

    @Override // TODO: Remove
    public LiveData<DayScheduleDto> getDaySchedule(String id, ZonedDateTime date) {

        DayScheduleDto dayScheduleDto = new DayScheduleDto();
        dayScheduleDto.setAreaName("Unknown");
        dayScheduleDto.setDate(date.getDayOfMonth() + " " + date.getMonth().name());
        dayScheduleDto.setDowntime("");
        dayScheduleDto.setEvents(new ArrayList<>());

        return new MutableLiveData<>(dayScheduleDto);
    }


}
