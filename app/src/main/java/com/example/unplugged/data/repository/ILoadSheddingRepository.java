package com.example.unplugged.data.repository;

import androidx.lifecycle.LiveData;

import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.AreaScheduleDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.StatusDto;

import java.util.List;

public interface ILoadSheddingRepository {

    LiveData<StatusDto> getStatus();

    LiveData<List<FoundAreaDto>> findAreas(String searchText);

    void observeArea(String id);

    LiveData<List<AreaDto>> getObservedAreas();

    LiveData<AreaScheduleDto> getObservedAreaSchedule(String id);

}
