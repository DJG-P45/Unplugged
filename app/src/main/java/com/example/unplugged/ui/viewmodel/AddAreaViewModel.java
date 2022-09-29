package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.repository.ILoadSheddingRepository;
import com.example.unplugged.data.repository.LoadSheddingRepository;
import com.example.unplugged.ui.state.FoundArea;

import java.util.ArrayList;
import java.util.List;

public class AddAreaViewModel extends BaseViewModel {

    private final ILoadSheddingRepository repository;
    private final MutableLiveData<List<FoundArea>> uiFoundAreas;

    public AddAreaViewModel(@NonNull Application application) {
        super(application);
        repository = new LoadSheddingRepository(application);
        uiFoundAreas = new MutableLiveData<>();
        initErrorFeed(repository.getErrorFeed());
    }

    public LiveData<List<FoundArea>> findAreas(String searchText) {
        LiveData<List<FoundAreaDto>> foundAreas = repository.findAreas(searchText);

        foundAreas.observeForever(foundAreaDtos -> {
            List<FoundArea> foundAreaList = new ArrayList<>();

            for (FoundAreaDto foundAreaDto : foundAreaDtos) {
                FoundArea uiFoundArea = new FoundArea();
                uiFoundArea.setFoundAreaDto(foundAreaDto);
                uiFoundArea.setObserveArea(() -> repository.observeArea(foundAreaDto.getId()));
                foundAreaList.add(uiFoundArea);
            }

            this.uiFoundAreas.setValue(foundAreaList);
        });

        return uiFoundAreas;
    }
}
