package com.example.unplugged.data.repository;

import androidx.lifecycle.LiveData;

import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.ScheduleDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.other.ErrorCategory;
import com.example.unplugged.ui.viewmodel.Consumable;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;


public interface ILoadSheddingRepository {

    void getStatus(ICallback<StatusDto> successCallback, ICallback<ErrorCategory> errorCallback);

    void findAreas(String searchText, ICallback<List<FoundAreaDto>> successCallback, ICallback<ErrorCategory> errorCallback);

    void observeArea(final String id, Runnable onPersisted);

    void removeObservedArea(final String id);

    void getObservedAreas(ICallback<AreaDto> successCallback, ICallback<ErrorCategory> errorCallback);

    void getDaySchedule(String areaId, LocalDate date, ICallback<DayScheduleDto> successCallback, ICallback<ErrorCategory> errorCallback);

}
