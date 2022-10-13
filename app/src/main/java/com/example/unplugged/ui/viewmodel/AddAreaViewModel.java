package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.BuildConfig;
import com.example.unplugged.data.datasource.EskomSePushNetworkApi;
import com.example.unplugged.data.datasource.PseudoEskomSePushNetworkApi;
import com.example.unplugged.data.datasource.UnpluggedDatabase;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.repository.ICallback;
import com.example.unplugged.data.repository.ILoadSheddingRepository;
import com.example.unplugged.data.repository.LoadSheddingRepository;
import com.example.unplugged.ui.state.FoundArea;

import java.util.ArrayList;
import java.util.List;

public class AddAreaViewModel extends BaseViewModel {

    private final ILoadSheddingRepository repository;
    private final MutableLiveData<List<FoundArea>> uiFoundAreas;
    private final MutableLiveData<Boolean> uiAreaAdded;

    public AddAreaViewModel(@NonNull Application application) {
        super(application);
        UnpluggedDatabase db = UnpluggedDatabase.getDatabase(application);
        uiFoundAreas = new MutableLiveData<>();
        uiAreaAdded = new MutableLiveData<>();
        repository = new LoadSheddingRepository(new EskomSePushNetworkApi(application, BuildConfig.ESKOM_SE_PUSH_KEY), db.observedAreaDao());
    }

    public LiveData<Boolean> isAreaAdded() {
        return uiAreaAdded;
    }

    public LiveData<List<FoundArea>> getFoundAreas() {
        return uiFoundAreas;
    }

    public void findAreas(String searchText) {
        repository.findAreas(searchText, foundAreasCallback(), errorCallback());
    }

    private ICallback<List<FoundAreaDto>> foundAreasCallback() {
        return foundAreaDtos -> {
            List<FoundArea> foundAreaList = new ArrayList<>();

            for (FoundAreaDto foundAreaDto : foundAreaDtos) {
                FoundArea uiFoundArea = new FoundArea();
                uiFoundArea.setFoundAreaDto(foundAreaDto);
                uiFoundArea.setObserveArea(() -> repository.observeArea(foundAreaDto.getId(), onAreaPersisted()));
                foundAreaList.add(uiFoundArea);
            }

            this.uiFoundAreas.setValue(foundAreaList);
        };
    }

    private Runnable onAreaPersisted() {
        return () -> uiAreaAdded.setValue(true);
    }
}
