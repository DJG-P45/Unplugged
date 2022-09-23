package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.dto.EventDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.repository.ILoadSheddingRepository;
import com.example.unplugged.data.repository.LoadSheddingRepository;
import com.example.unplugged.ui.state.Area;
import com.example.unplugged.ui.state.DaySchedule;
import com.example.unplugged.ui.state.Event;
import com.example.unplugged.ui.state.FoundArea;
import com.example.unplugged.ui.state.Status;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LoadSheddingViewModel extends AndroidViewModel {

    private final ILoadSheddingRepository repository;
    private final LiveData<List<AreaDto>> areas;
    private final LiveData<StatusDto> status;
    private LiveData<DayScheduleDto> daySchedule;
    private LiveData<List<FoundAreaDto>> foundAreas;
    private final MutableLiveData<Status> uiStatus;
    private final MutableLiveData<List<Area>> uiAreas;
    private final MutableLiveData<DaySchedule> uiDaySchedule;
    private final MutableLiveData<List<FoundArea>> uiFoundAreas;
    private String selectedAreaId;
    private ZonedDateTime currentScheduleDate;

    public LoadSheddingViewModel(@NonNull Application application) {
        super(application);
        repository = new LoadSheddingRepository();
        status = repository.getStatus();
        areas = repository.getObservedAreas();
        uiStatus = new MutableLiveData<>();
        uiAreas = new MutableLiveData<>();
        uiFoundAreas = new MutableLiveData<>();
        uiDaySchedule = new MutableLiveData<>();
        selectedAreaId = null;
        currentScheduleDate = ZonedDateTime.now();
    }

    public LiveData<Status> getStatus() {
        status.observeForever(statusDto -> {
            this.uiStatus.setValue(new Status(statusDto.getStage(), statusDto.getElapsedTime()));
        });

        return uiStatus;
    }

    public LiveData<List<FoundArea>> findAreas(String searchText) {
        foundAreas = repository.findAreas(searchText);

        foundAreas.observeForever(foundAreaDtos -> {
            List<FoundArea> uiFoundAreas = new ArrayList<>();

            for (FoundAreaDto foundAreaDto : foundAreaDtos) {
                FoundArea uiFoundArea = new FoundArea();
                uiFoundArea.setTitle(foundAreaDto.getName());
                uiFoundArea.setSubtitle(foundAreaDto.getRegion());
                uiFoundArea.setObserveArea(() -> repository.observeArea(foundAreaDto.getId()));
                uiFoundAreas.add(uiFoundArea);
            }

            this.uiFoundAreas.setValue(uiFoundAreas);
        });

        return uiFoundAreas;
    }

    public LiveData<List<Area>> getObservedAreas() {
        List<Area> areasBuffer = new ArrayList<>();

        areas.observeForever(areaDtos -> {
            for (AreaDto areaDto : areaDtos) {
                Area area = new Area();
                area.setName(areaDto.getName());
                area.setRegion(areaDto.getRegion());
                String event = areaDto.getEvent();
                area.setEvent(event);
                area.setSelectArea(() -> selectedAreaId = areaDto.getId());
                area.setRemoveArea(() -> repository.removeObservedArea(areaDto.getId()));
                areasBuffer.add(area);
            }
            uiAreas.setValue(areasBuffer);
        });

        return uiAreas;
    }

    public LiveData<DaySchedule> getDaySchedule() {
        loadDaySchedule();
        return uiDaySchedule;
    }

    private void loadDaySchedule() {
        daySchedule = repository.getDaySchedule(selectedAreaId, currentScheduleDate);

        daySchedule.observeForever(dayScheduleDto -> {
            DaySchedule schedule = new DaySchedule();

            // Set UI State action properties
            schedule.setTodaySchedule(loadTodaySchedule());
            schedule.setNextDaySchedule(loadNextDaySchedule());
            schedule.setPreviousDaySchedule(loadPreviousDaySchedule());

            // Set UI State value properties
            schedule.setAreaName(dayScheduleDto.getAreaName());
            schedule.setCurrentDate(dayScheduleDto.getDate());
            schedule.setDowntime(dayScheduleDto.getDowntime());

            List<Event> events = new ArrayList<>();
            for (EventDto eventDto : dayScheduleDto.getEvents()) {
                Event event = new Event();
                event.setStartTime(eventDto.getStart());
                event.setDurationInMinutes((int) eventDto.getDuration(ChronoUnit.MINUTES));
                event.setNote(eventDto.getNote());
                events.add(event);
            }

            // Update live data which will notify observers of this data.
            schedule.setEvents(events);
            uiDaySchedule.setValue(schedule);
        });
    }

    private Runnable loadTodaySchedule() {
        return () -> {
            currentScheduleDate = ZonedDateTime.now();
            loadDaySchedule();
        };
    }

    private Runnable loadNextDaySchedule() {
        return () -> {
            currentScheduleDate = currentScheduleDate.plusDays(1);
            loadDaySchedule();
        };
    }

    private Runnable loadPreviousDaySchedule() {
        return () -> {
            currentScheduleDate = currentScheduleDate.minusDays(1);
            loadDaySchedule();
        };
    }
}
