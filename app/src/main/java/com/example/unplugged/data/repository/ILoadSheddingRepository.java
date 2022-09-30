package com.example.unplugged.data.repository;

import androidx.lifecycle.LiveData;

import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.other.ErrorCategory;
import com.example.unplugged.ui.viewmodel.Consumable;

import java.time.LocalDate;
import java.util.List;

public interface ILoadSheddingRepository {

    LiveData<Consumable<ErrorCategory>> getErrorFeed();

    LiveData<StatusDto> getStatus();

    LiveData<List<FoundAreaDto>> findAreas(String searchText);

    void observeArea(final String id, Runnable onPersisted);

    void removeObservedArea(final String id);

    LiveData<List<AreaDto>> getObservedAreas();

    LiveData<AreaDto> getArea(String areaId);

    LiveData<DayScheduleDto> getDaySchedule(String areaId, LocalDate date);

}
