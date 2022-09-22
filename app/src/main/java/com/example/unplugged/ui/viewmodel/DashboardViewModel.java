package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.AreaScheduleDto;
import com.example.unplugged.data.dto.EventDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.repository.ILoadSheddingRepository;
import com.example.unplugged.data.repository.LoadSheddingRepository;
import com.example.unplugged.ui.state.Area;
import com.example.unplugged.ui.state.AreaSchedule;
import com.example.unplugged.ui.state.Event;
import com.example.unplugged.ui.state.Status;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private ILoadSheddingRepository repository;
    private LiveData<List<AreaDto>> areas;
    private LiveData<StatusDto> status;
    private LiveData<AreaScheduleDto> areaSchedule;
    private MutableLiveData<Status> uiStatus;
    private MutableLiveData<List<Area>> uiAreas;
    private MutableLiveData<AreaSchedule> uiAreaSchedule;
    private String selectedAreaId;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        repository = new LoadSheddingRepository();
        status = repository.getStatus();
        areas = repository.getObservedAreas();
        // TODO: What happens if selected area not fetched/found?
        areaSchedule = repository.getObservedAreaSchedule(selectedAreaId);
        uiStatus = new MutableLiveData<>();
        uiAreas = new MutableLiveData<>();
        uiAreaSchedule = new MutableLiveData<>();
        selectedAreaId = null;
    }

    public LiveData<Status> getStatus() {
        status.observeForever(statusDto -> {
            long timeSince = statusDto.getUpdated().until(ZonedDateTime.now(), ChronoUnit.HOURS);
            String line = ("Since " + timeSince + " hours ago");
            this.uiStatus.setValue(new Status(statusDto.getStage(), line));
        });

        return uiStatus;
    }

    public LiveData<List<Area>> getObservedAreas() {
        List<Area> areasBuffer = new ArrayList<>();

        areas.observeForever(areaDtos -> {
            for (AreaDto areaDto : areaDtos) {
                Area area = new Area();
                area.setName(areaDto.getName());
                area.setRegion(areaDto.getRegion());
                String event = areaDto.getEvent().getNote(); //TODO: Should include formatted times
                area.setEvent(event);
                area.setSelectAction(() -> {
                    selectedAreaId = areaDto.getId(); // TODO: Should this be thread sage?
                });
                areasBuffer.add(area);
            }
            uiAreas.setValue(areasBuffer);
        });

        return uiAreas;
    }

    public LiveData<AreaSchedule> getAreaSchedule() {
        areaSchedule.observeForever(areaScheduleDto -> {
            Area area = new Area();
            area.setName(areaScheduleDto.getArea().getName());
            area.setRegion(areaScheduleDto.getArea().getRegion());
            String event = areaScheduleDto.getArea().getEvent().getNote(); //TODO: Should include formatted times
            area.setEvent(event);

            List<Event> events = new ArrayList<>();
            for (EventDto eventDto : areaScheduleDto.getEvents()) {
                Event uiEvent = new Event();
                uiEvent.setNote(eventDto.getNote());
                uiEvent.setStartTime(eventDto.getStart());
                long duration = eventDto.getStart().until(eventDto.getEnd(), ChronoUnit.MINUTES);
                uiEvent.setDurationInMinutes((int)duration);
                events.add(uiEvent);
            }

            uiAreaSchedule.setValue(new AreaSchedule(area, events));
        });

        return uiAreaSchedule;
    }
}
