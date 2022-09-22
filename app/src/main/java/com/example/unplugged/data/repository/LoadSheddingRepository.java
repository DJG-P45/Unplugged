package com.example.unplugged.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.AreaScheduleDto;
import com.example.unplugged.data.dto.EventDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.ui.state.Event;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LoadSheddingRepository implements ILoadSheddingRepository {

    @Override
    public LiveData<StatusDto> getStatus() {
        ZonedDateTime updated = ZonedDateTime.now().minusDays(4);
        MutableLiveData<StatusDto> liveData = new MutableLiveData<>();
        liveData.setValue(new StatusDto("Stage 4", updated));

        return liveData;
    }

    @Override
    public LiveData<List<FoundAreaDto>> findAreas(String searchText) {
        return null;
    }

    @Override
    public void observeArea(String id) {

    }

    @Override
    public LiveData<List<AreaDto>> getObservedAreas() {
        List<AreaDto> areas = new ArrayList<>();

        AreaDto area = new AreaDto();
        area.setName("Area One");
        area.setRegion("Group One");
        EventDto event = new EventDto(ZonedDateTime.now(), ZonedDateTime.now().plusHours(2), "Loadshedding");
        area.setEvent(event);
        areas.add(area);

        MutableLiveData<List<AreaDto>> liveData = new MutableLiveData<>();
        liveData.setValue(areas);

        return liveData;
    }

    @Override
    public LiveData<AreaScheduleDto> getObservedAreaSchedule(String id) {
        AreaDto area = new AreaDto();
        area.setName("Area One");
        area.setRegion("Group One");
        EventDto event = new EventDto(ZonedDateTime.now(), ZonedDateTime.now().plusHours(2), "Loadshedding");
        area.setEvent(event);

        List<EventDto> events = new ArrayList<>();
        events.add(event);

        MutableLiveData<AreaScheduleDto> liveData = new MutableLiveData<>();
        liveData.setValue(new AreaScheduleDto(area, events));

        //TODO: Should handle null value potential;
        return liveData;
    }
}
