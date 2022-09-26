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

    public DayScheduleViewModel(@NonNull Application application) {
        super(application);

        repository = new LoadSheddingRepository(application);
        uiSchedule = new MutableLiveData<>();
    }

    public LiveData<DaySchedule> getDaySchedule(String areaId, LocalDate date) {
        repository.getDaySchedule(areaId, date).observeForever(dayScheduleDto -> {
            DaySchedule schedule = new DaySchedule();
            schedule.setSchedule(dayScheduleDto);

            // Set UI State action properties
            schedule.setTodaySchedule(() -> getDaySchedule(areaId, LocalDate.now()));
            schedule.setNextDaySchedule(() -> getDaySchedule(areaId, date.plusDays(1)));
            schedule.setPreviousDaySchedule(() -> getDaySchedule(areaId, date.minusDays(1)));
            uiSchedule.setValue(schedule);
        });
        return uiSchedule;
    }

}
