package com.example.unplugged.data.repository;

import androidx.annotation.NonNull;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

public class LoadSheddingRepository implements ILoadSheddingRepository {

    private final ObservedAreaDao observedAreaDao;
    private final LoadSheddingApi loadSheddingApi;
    private final ObjectMapper mapper;
    private final SchedulerProvider sp;

    public LoadSheddingRepository(LoadSheddingApi loadSheddingApi, ObservedAreaDao observedAreaDao) {
        this.loadSheddingApi = loadSheddingApi;
        this.observedAreaDao = observedAreaDao;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.sp = ScheduleProviderFactory.getScheduleProvider();
    }

    @Override
    public void observeArea(final String id, Runnable onPersisted) {
        doAsync(Single.create(emitter -> {
            if (!observedAreaDao.observedAreaExists(id)) {
                emitter.onSuccess(observedAreaDao.insert(new ObservedAreaEntity(id)));
            }
            emitter.onSuccess(0L);
        }), aLong -> onPersisted.run(), throwable -> {});
    }

    @Override
    public void removeObservedArea(final String id) {
        Single.create(emitter -> observedAreaDao.delete(id))
        .subscribeOn(sp.newThread())
        .observeOn(sp.mainThread())
        .subscribe();
    }

    @Override
    public void getStatus(ICallback<StatusDto> successCallback, ICallback<ErrorCategory> errorCallback) {
        doAsync(loadSheddingApi.getStatus(), s -> {
            try {
                successCallback.accept(mapper.readValue(s, StatusDto.class));
            } catch (JsonProcessingException e) {
                errorCallback.accept(ErrorCategory.FAILED_TO_FETCH_STATUS);
            }
        }, throwable -> errorCallback.accept(ErrorCategory.FAILED_TO_FETCH_STATUS));
    }

    @Override
    public void findAreas(String searchText, ICallback<List<FoundAreaDto>> successCallback, ICallback<ErrorCategory> errorCallback) {
        doAsync(loadSheddingApi.findAreas(searchText), s -> {
            try {
                FoundAreaDto[] foundAreaArray = mapper.readValue(s, FoundAreaDto[].class);
                successCallback.accept(Arrays.asList(foundAreaArray));
            } catch (JsonProcessingException e) {
                errorCallback.accept(ErrorCategory.FAILED_TO_FETCH_AREAS);
            }
        }, throwable -> errorCallback.accept(ErrorCategory.FAILED_TO_FETCH_AREAS));
    }

    @Override
    public void getObservedAreas(ICallback<AreaDto> successCallback, ICallback<ErrorCategory> errorCallback) {
        Observable<ObservedAreaEntity> listObservable = Observable.create(emitter -> {
            List<ObservedAreaEntity> list = observedAreaDao.getAllObservedAreas();
            list.forEach(emitter::onNext);
            emitter.onComplete();
        });

        listObservable.flatMap(areaEntity -> loadSheddingApi.getAreaInfo(areaEntity.getId()).toObservable())
                .subscribeOn(sp.newThread())
                .observeOn(sp.mainThread())
                .subscribe(s -> {successCallback.accept(mapper.readValue(s, AreaDto.class));
        }, throwable -> errorCallback.accept(ErrorCategory.FAILED_TO_FETCH_AREAS));
    }

    @Override
    public void getDaySchedule(final String areaId, LocalDate date, ICallback<DayScheduleDto> successCallback, ICallback<ErrorCategory> errorCallback) {
        doAsync(
            Single.zip(loadSheddingApi.getStatus(), loadSheddingApi.getAreaInfo(areaId), (status, areaInfo) -> {
                StatusDto statusDto = mapper.readValue(status, StatusDto.class);
                AreaDto areaDto = mapper.readValue(areaInfo, AreaDto.class);
                areaDto.setId(areaId);

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
                return schedule;
        }).doOnError(throwable -> errorCallback.accept(ErrorCategory.FAILED_TO_FETCH_SCHEDULE)), successCallback::accept, throwable -> errorCallback.accept(ErrorCategory.FAILED_TO_FETCH_SCHEDULE));
    }

    private <T> Disposable doAsync(@NonNull Single<T> single, @NonNull Consumer<? super T> onSuccess, @NonNull Consumer<? super Throwable > onError) {
        return single.subscribeOn(sp.newThread())
                .observeOn(sp.mainThread())
                .subscribe(onSuccess, onError);
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
