package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.datasource.EskomSePushNetworkApi;
import com.example.unplugged.data.datasource.PseudoEskomSePushNetworkApi;
import com.example.unplugged.data.datasource.UnpluggedDatabase;
import com.example.unplugged.data.repository.ILoadSheddingRepository;
import com.example.unplugged.data.repository.LoadSheddingRepository;
import com.example.unplugged.ui.state.DaySchedule;

import java.time.LocalDate;

public class DayScheduleViewModel extends BaseViewModel {

    private final ILoadSheddingRepository repository;
    private MutableLiveData<DaySchedule> uiSchedule;

    public DayScheduleViewModel(@NonNull Application application) {
        super(application);
        UnpluggedDatabase db = UnpluggedDatabase.getDatabase(application);
        repository = new LoadSheddingRepository(new EskomSePushNetworkApi(application), db.observedAreaDao());
        initErrorFeed(repository.getErrorFeed());
    }

    public LiveData<DaySchedule> getDaySchedule(String areaId) {
        if (uiSchedule == null) { // Initialise
            uiSchedule = new MutableLiveData<>();
            loadSchedule(areaId, LocalDate.now());
        }

        return uiSchedule;
    }

    private void loadSchedule(String areaId, LocalDate date) {
        repository.getDaySchedule(areaId, date).observeForever(dayScheduleDto -> {
            DaySchedule schedule = new DaySchedule();
            schedule.setSchedule(dayScheduleDto);

            // Set UI State action properties
            schedule.setTodaySchedule(() -> loadSchedule(areaId, LocalDate.now()));
            schedule.setNextDaySchedule(() -> loadSchedule(areaId, date.plusDays(1)));
            schedule.setPreviousDaySchedule(() -> loadSchedule(areaId, date.minusDays(1)));
            uiSchedule.setValue(schedule);
        });
    }

}
