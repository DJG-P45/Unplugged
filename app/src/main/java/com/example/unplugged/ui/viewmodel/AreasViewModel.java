package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.BuildConfig;
import com.example.unplugged.data.datasource.EskomSePushNetworkApi;
import com.example.unplugged.data.datasource.PseudoEskomSePushNetworkApi;
import com.example.unplugged.data.datasource.UnpluggedDatabase;
import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.repository.ICallback;
import com.example.unplugged.data.repository.ILoadSheddingRepository;
import com.example.unplugged.data.repository.LoadSheddingRepository;
import com.example.unplugged.ui.state.Area;
import com.example.unplugged.ui.state.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AreasViewModel extends BaseViewModel {

    private final ILoadSheddingRepository repository;
    private final MutableLiveData<Status> uiStatus;
    private final MutableLiveData<List<Area>> uiAreas;

    public AreasViewModel(@NonNull Application application) {
        super(application);
        UnpluggedDatabase db = UnpluggedDatabase.getDatabase(application);
        uiStatus = new MutableLiveData<>();
        uiAreas = new MutableLiveData<>(new ArrayList<>());
        repository = new LoadSheddingRepository(new EskomSePushNetworkApi(application, BuildConfig.ESKOM_SE_PUSH_KEY), db.observedAreaDao());
        repository.getStatus(statusCallback(), errorCallback());
        repository.getObservedAreas(areasCallback(), errorCallback());
    }

    public LiveData<Status> getStatus() {
        return uiStatus;
    }

    public LiveData<List<Area>> getObservedAreas() {
        return uiAreas;
    }

    private ICallback<StatusDto> statusCallback() {
        return statusDto -> uiStatus.setValue(new Status("Stage " + statusDto.getStage(), statusDto.getElapsedTime()));
    }

    private ICallback<AreaDto> areasCallback() {
        return areaDto -> {
            List<Area> areasBuffer = new ArrayList<>(Objects.requireNonNull(uiAreas.getValue()));
            Area area = new Area(areaDto, () -> repository.removeObservedArea(areaDto.getId()));
            areasBuffer.add(area);
            uiAreas.setValue(areasBuffer);
        };
    }
}
