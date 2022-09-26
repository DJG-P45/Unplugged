package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayDto;
import com.example.unplugged.data.dto.EventDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.OutageDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.repository.ILoadSheddingRepository;
import com.example.unplugged.data.repository.LoadSheddingRepository;
import com.example.unplugged.ui.state.Area;
import com.example.unplugged.ui.state.DaySchedule;
import com.example.unplugged.ui.state.Event;
import com.example.unplugged.ui.state.FoundArea;
import com.example.unplugged.ui.state.Status;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AreasViewModel extends AndroidViewModel {

    private final ILoadSheddingRepository repository;
    private final LiveData<List<AreaDto>> areas;
    private final LiveData<StatusDto> status;
    private final MutableLiveData<Status> uiStatus;
    private final MutableLiveData<List<Area>> uiAreas;

    public AreasViewModel(@NonNull Application application) {
        super(application);
        repository = new LoadSheddingRepository(application);
        status = repository.getStatus();
        areas = repository.getObservedAreas();
        uiStatus = new MutableLiveData<>();
        uiAreas = new MutableLiveData<>();
    }

    public LiveData<Status> getStatus() {
        status.observeForever(statusDto -> {
            this.uiStatus.setValue(new Status("Stage " + statusDto.getStage(), statusDto.getElapsedTime()));
        });

        return uiStatus;
    }

    public LiveData<List<Area>> getObservedAreas() {
        List<Area> areasBuffer = new ArrayList<>();

        areas.observeForever(areaDtos -> {
            for (AreaDto areaDto : areaDtos) {
                Area area = new Area();
                area.setId(area.getId());
                area.setName(areaDto.getInfo().getName());
                area.setRegion(areaDto.getInfo().getRegion());
                area.setEvent(areaDto.getEvents().get(0).toString()); // TODO Should check index size first;
                area.setRemoveArea(() -> repository.removeObservedArea(areaDto.getId()));
                areasBuffer.add(area);
            }
            uiAreas.setValue(areasBuffer);
        });

        return uiAreas;
    }
}
