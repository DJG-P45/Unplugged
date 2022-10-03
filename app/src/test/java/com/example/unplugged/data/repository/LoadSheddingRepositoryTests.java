package com.example.unplugged.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.datasource.LoadSheddingApi;
import com.example.unplugged.data.datasource.ObservedAreaDao;
import com.example.unplugged.data.dto.AreaDto;
import com.example.unplugged.data.dto.DayDto;
import com.example.unplugged.data.dto.DayScheduleDto;
import com.example.unplugged.data.dto.EventDto;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.InfoDto;
import com.example.unplugged.data.dto.OutageDto;
import com.example.unplugged.data.dto.ScheduleDto;
import com.example.unplugged.data.dto.StageDto;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.data.entity.ObservedAreaEntity;
import com.example.unplugged.data.other.ScheduleProviderFactory;
import com.example.unplugged.data.other.TestSchedulerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

@RunWith(MockitoJUnitRunner.class)
public class LoadSheddingRepositoryTests {

    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

    @Mock
    private ObservedAreaDao observedAreaDao;

    @Mock
    private LoadSheddingApi loadSheddingApi;

    @Spy
    private ObjectMapper mapper;

    private LoadSheddingRepository repository;

    @BeforeClass
    public static void setup() {
        MockedStatic<ScheduleProviderFactory> factory = Mockito.mockStatic(ScheduleProviderFactory.class);
        factory.when(ScheduleProviderFactory::getScheduleProvider).thenReturn(new TestSchedulerProvider());
    }

    @Before
    public void beforeEachTest() {
        repository = new LoadSheddingRepository(loadSheddingApi, observedAreaDao);
    }

    @Test
    public void getStatus_whenCorrectJsonResponse_StatusLiveDataUpdates() {
        // Given
        final String response = "{\"name\":\"National\",\"next_stages\":[{\"stage\":\"2\",\"stage_start_timestamp\":\"2022-08-08T16:00:00+02:00\"},{\"stage\":\"0\",\"stage_start_timestamp\":\"2022-08-09T00:00:00+02:00\"}],\"stage\":\"1\",\"stage_updated\":\"2022-08-08T16:12:53.725852+02:00\"}";

        // When
        when(loadSheddingApi.getStatus()).thenReturn(Single.just(response));

        // Then
        LiveData<StatusDto> status = repository.getStatus();

        status.observeForever(statusDto -> {
            assertEquals(statusDto.getStage(), 1);
        });

    }

    @Test
    public void getStatus_whenIncorrectJsonResponse_StatusLiveDataNoUpdates() {
        // When
        when(loadSheddingApi.getStatus()).thenReturn(Single.just(""));

        // Then
        LiveData<StatusDto> status = repository.getStatus();

        status.observeForever(statusDto -> fail());
    }

    @Test
    public void findAreas_whenCorrectJsonResponse_StatusLiveDataUpdates() {
        // Given
        final String response = "[ { \"id\": \"westerncape-2-stellenboschmunicipality\", \"name\": \"Stellenbosch Municipality (2)\", \"region\": \"Western Cape\" }, { \"id\": \"westerncape-8-stellenboschfarmers\", \"name\": \"Stellenbosch farmers (8)\", \"region\": \"Western Cape\" }, { \"id\": \"eskomdirect-5215-stellenboschpart1outlyingstellenboschwesterncape\", \"name\": \"Stellenbosch Part 1 Outlying\", \"region\": \"Eskom Direct (Web), Stellenbosch, Western Cape\" }]";

        // When
        when(loadSheddingApi.findAreas(anyString())).thenReturn(Single.just(response));

        // Then
        LiveData<List<FoundAreaDto>> foundAreas = repository.findAreas("text");

        foundAreas.observeForever(foundAreaDtos -> {
            assertEquals(foundAreaDtos.size(), 3);
        });
    }

    @Test
    public void findAreas_whenIncorrectJsonResponse_FoundAreasLiveDataNoUpdates() {
        // When
        when(loadSheddingApi.findAreas(anyString())).thenReturn(Single.just(""));

        // Then
        LiveData<List<FoundAreaDto>> foundAreas = repository.findAreas("text");

        foundAreas.observeForever(statusDto -> fail());
    }

    @Test
    public void getAreaInfo_whenCorrectJsonResponse_AreaInfoLiveDataUpdates() {
        // Given
        final String response = "{\"events\": [{\"end\":\"2022-08-08T22:30:00+02:00\",\"note\":\"Stage 2\",\"start\":\"2022-08-08T20:00:00+02:00\"}],\"info\": {\"name\": \"Sandton-WEST (4)\",\"region\": \"Eskom Direct, City of Johannesburg, Gauteng\"},\"schedule\": {\"days\":[{\"date\":\"2022-08-08\",\"name\": \"Monday\",\"stages\": [[],[\"20:00-22:30\"],[\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-00:30\"],[\"04:00-06:30\",\"12:00-16:30\",\"20:00-00:30\"],[\"04:00-08:30\",\"12:00-16:30\",\"20:00-00:30\"]]}],\"source\":\"https://loadshedding.eskom.co.za/\"}}";

        // When
        when(loadSheddingApi.getAreaInfo(anyString())).thenReturn(Single.just(response));

        // Then
        LiveData<AreaDto> area = repository.getArea("text");

        area.observeForever(areaDto -> {
            assertEquals(areaDto.getId(), "text");
            assertEquals(areaDto.getEvents().size(), 1);
            assertEquals(areaDto.getSchedule().getDays().size(), 1);
        });
    }

    @Test
    public void getAreaInfo_whenIncorrectJsonResponse_AreaInfoNoLiveDataUpdates() {
        // When
        when(loadSheddingApi.getAreaInfo(anyString())).thenReturn(Single.just(""));

        // Then
        LiveData<AreaDto> area = repository.getArea("text");

        area.observeForever(statusDto -> fail());
    }

    @Test
    public void getObservedAreas_whenCorrectJsonResponse_ObservedAreasLiveDataUpdates() {
        // Given
        final ObservedAreaEntity observedAreaEntity = new ObservedAreaEntity("id-here");
        List<ObservedAreaEntity> observedAreaEntities = new ArrayList<>();
        observedAreaEntities.add(observedAreaEntity);
        final String response = "{\"events\": [{\"end\":\"2022-08-08T22:30:00+02:00\",\"note\":\"Stage 2\",\"start\":\"2022-08-08T20:00:00+02:00\"}],\"info\": {\"name\": \"Sandton-WEST (4)\",\"region\": \"Eskom Direct, City of Johannesburg, Gauteng\"},\"schedule\": {\"days\":[{\"date\":\"2022-08-08\",\"name\": \"Monday\",\"stages\": [[],[\"20:00-22:30\"],[\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-00:30\"],[\"04:00-06:30\",\"12:00-16:30\",\"20:00-00:30\"],[\"04:00-08:30\",\"12:00-16:30\",\"20:00-00:30\"]]}],\"source\":\"https://loadshedding.eskom.co.za/\"}}";

        // When
        when(observedAreaDao.getAllObservedAreas()).thenReturn(observedAreaEntities);
        when(loadSheddingApi.getAreaInfo(anyString())).thenReturn(Single.just(response));

        // Then
        LiveData<List<AreaDto>> areas = repository.getObservedAreas();

        areas.observeForever(areaDtoList -> {
            assertEquals(areaDtoList.size(), 1);
        });
    }

    @Test
    public void getObservedAreas_whenNoObservedEntities_ObservedAreasNoLiveDataUpdates() {
        // Given
        List<ObservedAreaEntity> observedAreaEntities = new ArrayList<>();

        // When
        when(observedAreaDao.getAllObservedAreas()).thenReturn(observedAreaEntities);

        // Then
        LiveData<List<AreaDto>> areas = repository.getObservedAreas();

        areas.observeForever(areaDtoList -> assertEquals(areaDtoList.size(), 0));
        verify(loadSheddingApi, times(0)).getAreaInfo(anyString());
    }

    @Test
    public void getObservedAreas_whenIncorrectJsonResponse_ObservedAreasNoLiveDataUpdates() {
        // Given
        final ObservedAreaEntity observedAreaEntity = new ObservedAreaEntity("id-here");
        List<ObservedAreaEntity> observedAreaEntities = new ArrayList<>();
        observedAreaEntities.add(observedAreaEntity);


        // When
        when(observedAreaDao.getAllObservedAreas()).thenReturn(observedAreaEntities);
        when(loadSheddingApi.getAreaInfo(anyString())).thenReturn(Single.just(""));

        // Then
        LiveData<List<AreaDto>> areas = repository.getObservedAreas();

        areas.observeForever(areaDtoList -> assertEquals(areaDtoList.size(), 0));
    }

    @Test
    public void getDaySchedule_whenStatusAndAreaRetrieved_DayScheduleLiveDataUpdates() {
        // Given
        LoadSheddingRepository loadSheddingRepository = spy(repository);

        final StatusDto statusDto = new StatusDto();
        statusDto.setUpdated(ZonedDateTime.now());
        statusDto.setStage(0);

        final EventDto eventDto = new EventDto();
        eventDto.setStart(ZonedDateTime.parse("2022-08-08T20:00:00+02:00"));
        eventDto.setEnd(ZonedDateTime.parse("2022-08-08T22:30:00+02:00"));
        eventDto.setNote("Stage 2");
        List<EventDto> eventDtos = new ArrayList<>();
        eventDtos.add(eventDto);

        final InfoDto infoDto = new InfoDto();
        infoDto.setName("Area");
        infoDto.setRegion("Region");

        final OutageDto outageDto = new OutageDto();
        outageDto.setStart(LocalTime.parse("20:00"));
        outageDto.setEnd(LocalTime.parse("22:30"));
        List<OutageDto> outages = new ArrayList<>();
        outages.add(outageDto);

        final StageDto stageDto = new StageDto();
        stageDto.setOutages(outages);
        List<StageDto> stages = new ArrayList<>();
        stages.add(stageDto);

        final DayDto dayDto = new DayDto();
        dayDto.setDate(LocalDate.now());
        dayDto.setName("Stage 2");
        dayDto.setStages(stages);
        List<DayDto> days = new ArrayList<>();
        days.add(dayDto);

        final ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setDays(days);

        final AreaDto areaDto = new AreaDto();
        areaDto.setId("id-here");
        areaDto.setEvents(eventDtos);
        areaDto.setInfo(infoDto);
        areaDto.setSchedule(scheduleDto);

        // When
        doReturn(new MutableLiveData<>(statusDto)).when(loadSheddingRepository).getStatus();
        doReturn(new MutableLiveData<>(areaDto)).when(loadSheddingRepository).getArea(anyString());

        // Then
        LiveData<DayScheduleDto> daySchedule = loadSheddingRepository.getDaySchedule("id-here", LocalDate.now());

        daySchedule.observeForever(dayScheduleDto -> {
            assertEquals(dayScheduleDto.getOutages().size(), 1);
            assertEquals(dayScheduleDto.getOutages().get(0).getStart(), LocalTime.parse("20:00"));
            assertEquals(dayScheduleDto.getOutages().get(0).getEnd(), LocalTime.parse("22:30"));
            assertEquals(dayScheduleDto.getAreaName(), "Area");
            assertEquals(dayScheduleDto.getDowntime(), "Downtime: 2h 30m");
        });
    }

    @Test
    public void getDaySchedule_whenNoStatusRetrieved_DayScheduleNoLiveDataUpdates() {
        // Given
        LoadSheddingRepository loadSheddingRepository = spy(repository);

        // When
        doReturn(new MutableLiveData<>()).when(loadSheddingRepository).getStatus();

        // Then
        LiveData<DayScheduleDto> daySchedule = loadSheddingRepository.getDaySchedule("id-here", LocalDate.now());

        daySchedule.observeForever(dayScheduleDto -> fail());
        verify(loadSheddingRepository, times(0)).getArea(anyString());
    }

    @Test
    public void getDaySchedule_whenNoAreaRetrieved_DayScheduleNoLiveDataUpdates() {
        // Given
        LoadSheddingRepository loadSheddingRepository = spy(repository);

        final StatusDto statusDto = new StatusDto();
        statusDto.setUpdated(ZonedDateTime.now());
        statusDto.setStage(0);

        // When
        doReturn(new MutableLiveData<>(statusDto)).when(loadSheddingRepository).getStatus();
        doReturn(new MutableLiveData<>()).when(loadSheddingRepository).getArea(anyString());

        // Then
        LiveData<DayScheduleDto> daySchedule = loadSheddingRepository.getDaySchedule("id-here", LocalDate.now());

        daySchedule.observeForever(dayScheduleDto -> fail());
    }
}
