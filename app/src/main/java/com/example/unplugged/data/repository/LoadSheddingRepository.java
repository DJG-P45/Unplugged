package com.example.unplugged.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.datasource.ApiException;
import com.example.unplugged.data.datasource.ApiNetworkException;
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
import com.example.unplugged.data.other.PrettyTime;
import com.example.unplugged.data.other.ScheduleProviderFactory;
import com.example.unplugged.data.other.SchedulerProvider;
import com.example.unplugged.ui.viewmodel.Consumable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoadSheddingRepository implements ILoadSheddingRepository {

    private final ObservedAreaDao observedAreaDao;
    private final LoadSheddingApi loadSheddingApi;
    private final ObjectMapper mapper;
    private final MutableLiveData<Consumable<ErrorCategory>> errorFeed;
    private final SchedulerProvider sp;

    public LoadSheddingRepository(LoadSheddingApi loadSheddingApi, ObservedAreaDao observedAreaDao) {
        this.loadSheddingApi = loadSheddingApi;
        this.observedAreaDao = observedAreaDao;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.errorFeed = new MutableLiveData<>();
        this.sp = ScheduleProviderFactory.getScheduleProvider();
    }

    @Override
    public LiveData<Consumable<ErrorCategory>> getErrorFeed() {
        return errorFeed;
    }

    @Override
    public void observeArea(final String id, Runnable onPersisted) {
        Single.<Long>create(emitter -> {
            if (!observedAreaDao.observedAreaExists(id)) {
                emitter.onSuccess(observedAreaDao.insert(new ObservedAreaEntity(id)));
            }
            emitter.onSuccess(0L);
        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aLong -> onPersisted.run());
    }

    @Override
    public void removeObservedArea(final String id) {
        Single.create(emitter -> observedAreaDao.delete(id))
        .subscribeOn(sp.newThread())
        .observeOn(sp.mainThread())
        .subscribe();
    }

    @Override
    public LiveData<StatusDto> getStatus() {
        MutableLiveData<StatusDto> mutableLiveData = new MutableLiveData<>();

        loadSheddingApi.getStatus()
                .subscribeOn(sp.newThread())
                .observeOn(sp.mainThread())
                .subscribe(s -> {
                    try {
                        mutableLiveData.setValue(mapper.readValue(s, StatusDto.class));
                    } catch (JsonProcessingException e) {
                        processError(e);
                    }
                }, this::processError);

        return mutableLiveData;
    }

    @Override
    public LiveData<List<FoundAreaDto>> findAreas(final String searchText) {
        MutableLiveData<List<FoundAreaDto>> mutableLiveData = new MutableLiveData<>();

        loadSheddingApi.findAreas(searchText)
                .subscribeOn(sp.newThread())
                .observeOn(sp.mainThread())
                .subscribe(s -> {
                    try {
                        FoundAreaDto[] foundAreaArray = mapper.readValue(s, FoundAreaDto[].class);
                        mutableLiveData.setValue(Arrays.asList(foundAreaArray));
                    } catch (JsonProcessingException e) {
                        processError(e);
                    }
                }, this::processError);

        return mutableLiveData;
    }

    @Override
    public LiveData<List<AreaDto>> getObservedAreas() {
        MutableLiveData<List<AreaDto>> mutableLiveData = new MutableLiveData<>(new ArrayList<>());

        Single.<List<ObservedAreaEntity>>create(emitter -> {
            emitter.onSuccess(observedAreaDao.getAllObservedAreas());
        }).subscribeOn(sp.newThread())
        .observeOn(sp.mainThread())
        .subscribe(observedAreaEntities -> {
            for (ObservedAreaEntity entity : observedAreaEntities) {
                getArea(entity.getId()).observeForever(areaDto -> {
                    List<AreaDto> areas = mutableLiveData.getValue();
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

        loadSheddingApi.getAreaInfo(areaId)
                .subscribeOn(sp.newThread())
                .observeOn(sp.mainThread())
                .subscribe(s -> {
                    try {
                        AreaDto areaDto = mapper.readValue(s, AreaDto.class);
                        areaDto.setId(areaId);
                        mutableLiveData.setValue(areaDto);
                    } catch (JsonProcessingException e) {
                        processError(e);
                    }
                }, this::processError);

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
        throwable.printStackTrace();

        ErrorCategory errorCategory = ErrorCategory.UNKNOWN;

        if (throwable instanceof ApiException) {
            errorCategory = ErrorCategory.SERVICE;
        } else if (throwable instanceof ApiNetworkException) {
            errorCategory = ErrorCategory.NETWORK;
        } else if (throwable instanceof JsonProcessingException) {
            errorCategory = ErrorCategory.SERVICE;
        }

        errorFeed.postValue(new Consumable<>(errorCategory));
    }

    private static String totalDowntime(List<OutageDto> outages) {

        if (outages.size() == 0) return "No downtime";

        int totalTimeInMinutes = 0;

        for (OutageDto outageDto: outages) {
            totalTimeInMinutes += outageDto.getDurationInMinutes();
        }

        Duration duration = Duration.ofMinutes(totalTimeInMinutes);
        return   "Downtime: " + PrettyTime.beautify(duration);
    }
}
