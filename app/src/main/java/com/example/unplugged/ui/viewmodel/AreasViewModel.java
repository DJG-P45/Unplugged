package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.datasource.EskomSePushNetworkApi;
import com.example.unplugged.data.datasource.PseudoEskomSePushNetworkApi;
import com.example.unplugged.data.datasource.UnpluggedDatabase;
import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.repository.ILoadSheddingRepository;
import com.example.unplugged.repository.LoadSheddingRepository;
import com.example.unplugged.ui.state.Area;
import com.example.unplugged.ui.state.Status;

import java.util.ArrayList;
import java.util.List;

public class AreasViewModel extends BaseViewModel {

    private final ILoadSheddingRepository repository;
    private final LiveData<List<AreaDto>> areas;
    private final LiveData<StatusDto> status;
    private final MutableLiveData<Status> uiStatus;
    private final MutableLiveData<List<Area>> uiAreas;

    public AreasViewModel(@NonNull Application application) {
        super(application);
        UnpluggedDatabase db = UnpluggedDatabase.getDatabase(application);
        repository = new LoadSheddingRepository(new PseudoEskomSePushNetworkApi(), db.observedAreaDao());
        status = repository.getStatus();
        areas = repository.getObservedAreas();
        uiStatus = new MutableLiveData<>();
        uiAreas = new MutableLiveData<>();
        initErrorFeed(repository.getErrorFeed());
    }

    public LiveData<Status> getStatus() {
        status.observeForever(statusDto -> {
            this.uiStatus.setValue(new Status("Stage " + statusDto.getStage(), statusDto.getElapsedTime()));
        });

        return uiStatus;
    }

    public LiveData<List<Area>> getObservedAreas() {
        areas.observeForever(areaDtos -> {
            List<Area> areasBuffer = new ArrayList<>();
            for (AreaDto areaDto : areaDtos) {
                Area area = new Area(areaDto, () -> repository.removeObservedArea(areaDto.getId()));
                areasBuffer.add(area);
            }
            uiAreas.setValue(areasBuffer);
        });

        return uiAreas;
    }
}
