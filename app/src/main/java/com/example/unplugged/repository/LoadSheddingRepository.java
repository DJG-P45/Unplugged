package com.example.unplugged.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.datasource.Callback;
import com.example.unplugged.data.datasource.LoadSheddingApi;
import com.example.unplugged.data.datasource.ObservedAreaDao;
import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.OutageDto;
import com.example.unplugged.data.dto.StageDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.entity.ObservedAreaEntity;
import com.example.unplugged.data.other.ErrorCategory;
import com.example.unplugged.ui.viewmodel.Consumable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoadSheddingRepository implements ILoadSheddingRepository {

    private final ObservedAreaDao observedAreaDao;
    private final LoadSheddingApi loadSheddingApi;
    private final ObjectMapper mapper;
    private final MutableLiveData<Consumable<ErrorCategory>> errorFeed;

    public LoadSheddingRepository(LoadSheddingApi loadSheddingApi, ObservedAreaDao observedAreaDao) {
        this.loadSheddingApi = loadSheddingApi;
        this.observedAreaDao = observedAreaDao;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.errorFeed = new MutableLiveData<>();
    }

    @Override
    public LiveData<Consumable<ErrorCategory>> getErrorFeed() {
        return errorFeed;
    }

    @Override
    public void observeArea(final String id) {
        Observable.create(emitter -> {
            if (!observedAreaDao.observedAreaExists(id)) observedAreaDao.insert(new ObservedAreaEntity(id));
        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
    }

    @Override
    public void removeObservedArea(final String id) {
        Observable.create(emitter -> observedAreaDao.delete(id))
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
    }

    @Override
    public LiveData<StatusDto> getStatus() {
        MutableLiveData<StatusDto> mutableLiveData = new MutableLiveData<>();

        Observable.create(emitter -> {
            loadSheddingApi.getStatus(new Callback() {
                @Override
                public void onResponse(String json) {
                    try {
                        StatusDto statusDto = mapper.readValue(json, StatusDto.class);
                        emitter.onNext(statusDto);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        emitter.onError(e);
                    }
                }

                @Override
                public void onError(String error) {
                    emitter.onError(new Exception());
                }
            });
        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> mutableLiveData.setValue((StatusDto) o), this::processError);

        return mutableLiveData;
    }

    @Override
    public LiveData<List<FoundAreaDto>> findAreas(String searchText) {
        MutableLiveData<List<FoundAreaDto>> mutableLiveData = new MutableLiveData<>();

        Observable.create(emitter -> {
            loadSheddingApi.findAreas(searchText, new Callback() {
                @Override
                public void onResponse(String json) {
                    try {
                        FoundAreaDto[] foundAreaArray = mapper.readValue(json, FoundAreaDto[].class);
                        emitter.onNext(Arrays.asList(foundAreaArray));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    emitter.onError(new Exception());
                }
            });
        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> mutableLiveData.setValue((List<FoundAreaDto>) o), this::processError);

        return mutableLiveData;
    }

    @Override
    public LiveData<List<AreaDto>> getObservedAreas() {
        MutableLiveData<List<AreaDto>> mutableLiveData = new MutableLiveData<>();

        observedAreaDao.getAllObservedAreas().observeForever(observedAreas -> {
            List<AreaDto> areas = new ArrayList<>();

            for (ObservedAreaEntity entity : observedAreas) {
                getArea(entity.getId()).observeForever(areaDto -> {
                    areas.add(areaDto);
                    mutableLiveData.setValue(areas);
                });
            }
        });

        return mutableLiveData;
    }

    @Override
    public LiveData<AreaDto> getArea(String areaId) {
        MutableLiveData<AreaDto> mutableLiveData = new MutableLiveData<>();

        Observable.create(emitter -> {
            loadSheddingApi.getAreaInfo(areaId, new Callback() {
                @Override
                public void onResponse(String json) {
                    try {
                        AreaDto areaDto = mapper.readValue(json, AreaDto.class);
                        areaDto.setId(areaId);
                        emitter.onNext(areaDto);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    emitter.onError(new Exception());
                }
            });
        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> mutableLiveData.setValue((AreaDto) o), this::processError);

        return mutableLiveData;
    }

    @Override
    public LiveData<DayScheduleDto> getDaySchedule(final String areaId, LocalDate date) {
        MutableLiveData<DayScheduleDto> mutableLiveData = new MutableLiveData<>();

        getStatus().observeForever(statusDto -> getArea(areaId).observeForever(areaDto -> {
            DayScheduleDto schedule = new DayScheduleDto();
            schedule.setAreaName(areaDto.getInfo().getName());
            schedule.setDate(date.getDayOfMonth() + " " + date.getMonth().name());
            schedule.setDowntime("No downtime");
            schedule.setOutages(new ArrayList<>());

            for (DayDto dayDto : areaDto.getSchedule().getDays()) {
                if (dayDto.getDate().isEqual(date)) {
                    StageDto stageDto = dayDto.getStages().get(statusDto.getStage());
                    schedule.setOutages(stageDto.getOutages());
                    schedule.setDowntime(totalDowntime(stageDto.getOutages()));
                }
            }
            mutableLiveData.postValue(schedule);
        }));

        return  mutableLiveData;
    }


    private void processError(Throwable throwable) {
        if (throwable instanceof JsonProcessingException) {
            errorFeed.postValue(new Consumable<>(ErrorCategory.SYSTEM));
        }
        errorFeed.postValue(new Consumable<>(ErrorCategory.NETWORK));
    }

    private static String totalDowntime(List<OutageDto> outages) {

        if (outages.size() == 0) return "No downtime";

        int totalTimeInMinutes = 0;

        for (OutageDto outageDto: outages) {
            totalTimeInMinutes += outageDto.getDurationInMinutes();
        }

        int hours = totalTimeInMinutes / 60;
        int minutes = totalTimeInMinutes % 60;

        String result = "Downtime:";

        if (hours > 1) result += " " + hours + " hours";
        if (hours == 1) result += " " + hours + " hour";
        if (minutes > 1) result += " " + minutes + " minutes";
        if (minutes == 1) result += " " + minutes + " minute";

        return   result;
    }
}
