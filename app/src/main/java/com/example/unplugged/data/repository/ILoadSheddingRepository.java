package com.example.unplugged.data.repository;

import androidx.lifecycle.LiveData;

import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.StatusDto;

import java.time.ZonedDateTime;
import java.util.List;

public interface ILoadSheddingRepository {

    LiveData<StatusDto> getStatus();

    LiveData<List<FoundAreaDto>> findAreas(String searchText);

    void observeArea(String id);

    void removeObservedArea(String id);

    LiveData<List<AreaDto>> getObservedAreas();

    LiveData<DayScheduleDto> getDaySchedule(String id, ZonedDateTime date);

}
