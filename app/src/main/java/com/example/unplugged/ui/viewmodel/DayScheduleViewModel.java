package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.repository.ILoadSheddingRepository;
import com.example.unplugged.data.repository.LoadSheddingRepository;
import com.example.unplugged.ui.state.DaySchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DayScheduleViewModel extends AndroidViewModel {

    private final ILoadSheddingRepository repository;
    private final MutableLiveData<DaySchedule> uiSchedule;
    private LocalDate scheduleDate;
    private final MutableLiveData<AreaDto> area;

    public DayScheduleViewModel(@NonNull Application application) {
        super(application);

        repository = new LoadSheddingRepository(application);
        area = new MutableLiveData<>();
        uiSchedule = new MutableLiveData<>();
        scheduleDate = LocalDate.now();
    }

    public LiveData<DaySchedule> getDaySchedule(String areaId) {
        repository.getArea(areaId).observeForever(area::setValue);
        loadDaySchedule();
        return uiSchedule;
    }

    private void loadDaySchedule() {
        area.observeForever(areaDto -> {
            DaySchedule schedule = new DaySchedule();

            // Set UI State action properties
            schedule.setTodaySchedule(loadTodaySchedule());
            schedule.setNextDaySchedule(loadNextDaySchedule());
            schedule.setPreviousDaySchedule(loadPreviousDaySchedule());

            // Set UI State value properties
            repository.getDaySchedule(areaDto, scheduleDate).observeForever(dayScheduleDto -> {
                schedule.setSchedule(dayScheduleDto);
                uiSchedule.setValue(schedule);
            });

        });
    }

    private Runnable loadTodaySchedule() {
        return () -> {
            scheduleDate = LocalDate.now();
            loadDaySchedule();
        };
    }

    private Runnable loadNextDaySchedule() {
        return () -> {
            scheduleDate = scheduleDate.plusDays(1);
            loadDaySchedule();
        };
    }

    private Runnable loadPreviousDaySchedule() {
        return () -> {
            scheduleDate = scheduleDate.minusDays(1);
            loadDaySchedule();
        };
    }
}
