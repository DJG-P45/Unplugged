package com.example.unplugged.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.unplugged.data.datasource.LoadSheddingApi;
import com.example.unplugged.data.datasource.ObservedAreaDao;
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
    public void getStatus_whenCorrectJsonResponse_StatusSuccessCallback() {
        // Given
        final String response = "{\"name\":\"National\",\"next_stages\":[{\"stage\":\"2\",\"stage_start_timestamp\":\"2022-08-08T16:00:00+02:00\"},{\"stage\":\"0\",\"stage_start_timestamp\":\"2022-08-09T00:00:00+02:00\"}],\"stage\":\"1\",\"stage_updated\":\"2022-08-08T16:12:53.725852+02:00\"}";

        // When
        when(loadSheddingApi.getStatus()).thenReturn(Single.just(response));

        // Then
        repository.getStatus(value -> assertEquals(value.getStage(), 1), value -> {});
    }

    @Test
    public void getStatus_whenIncorrectJsonResponse_StatusErrorCallback() {
        // When
        when(loadSheddingApi.getStatus()).thenReturn(Single.just(""));

        // Then
        repository.getStatus(value -> fail(), value -> fail());
    }

    @Test
    public void findAreas_whenCorrectJsonResponse_FoundAreasSuccessCallback() {
        // Given
        final String response = "[ { \"id\": \"westerncape-2-stellenboschmunicipality\", \"name\": \"Stellenbosch Municipality (2)\", \"region\": \"Western Cape\" }, { \"id\": \"westerncape-8-stellenboschfarmers\", \"name\": \"Stellenbosch farmers (8)\", \"region\": \"Western Cape\" }, { \"id\": \"eskomdirect-5215-stellenboschpart1outlyingstellenboschwesterncape\", \"name\": \"Stellenbosch Part 1 Outlying\", \"region\": \"Eskom Direct (Web), Stellenbosch, Western Cape\" }]";

        // When
        when(loadSheddingApi.findAreas(anyString())).thenReturn(Single.just(response));

        // Then
        repository.findAreas("text", value -> assertEquals(value.size(), 3), value -> {});
    }

    @Test
    public void findAreas_whenIncorrectJsonResponse_FoundAreasErrorCallback() {
        // When
        when(loadSheddingApi.findAreas(anyString())).thenReturn(Single.just(""));

        // Then
        repository.findAreas("text", value -> fail(), value -> fail());
    }

    @Test
    public void getObservedAreas_whenCorrectJsonResponse_ObservedAreasSuccessCallback() {
        // Given
        final ObservedAreaEntity observedAreaEntity = new ObservedAreaEntity("id-here");
        List<ObservedAreaEntity> observedAreaEntities = new ArrayList<>();
        observedAreaEntities.add(observedAreaEntity);
        final String response = "{\"events\": [{\"end\":\"2022-08-08T22:30:00+02:00\",\"note\":\"Stage 2\",\"start\":\"2022-08-08T20:00:00+02:00\"}],\"info\": {\"name\": \"Sandton-WEST (4)\",\"region\": \"Eskom Direct, City of Johannesburg, Gauteng\"},\"schedule\": {\"days\":[{\"date\":\"2022-08-08\",\"name\": \"Monday\",\"stages\": [[],[\"20:00-22:30\"],[\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-00:30\"],[\"04:00-06:30\",\"12:00-16:30\",\"20:00-00:30\"],[\"04:00-08:30\",\"12:00-16:30\",\"20:00-00:30\"]]}],\"source\":\"https://loadshedding.eskom.co.za/\"}}";

        // When
        when(observedAreaDao.getAllObservedAreas()).thenReturn(observedAreaEntities);
        when(loadSheddingApi.getAreaInfo(anyString())).thenReturn(Single.just(response));

        // Then
        repository.getObservedAreas(value -> assertEquals(value.getInfo().getName(), "Sandton-WEST (4)"), value -> fail());
    }

    @Test
    public void getObservedAreas_whenNoObservedEntities_ObservedAreasErrorCallback() {
        // Given
        List<ObservedAreaEntity> observedAreaEntities = new ArrayList<>();

        // When
        when(observedAreaDao.getAllObservedAreas()).thenReturn(observedAreaEntities);

        // Then
        repository.getObservedAreas(value -> fail(), value -> fail());
    }

    @Test
    public void getObservedAreas_whenIncorrectJsonResponse_ObservedAreasErrorCallback() {
        // Given
        final ObservedAreaEntity observedAreaEntity = new ObservedAreaEntity("id-here");
        List<ObservedAreaEntity> observedAreaEntities = new ArrayList<>();
        observedAreaEntities.add(observedAreaEntity);


        // When
        when(observedAreaDao.getAllObservedAreas()).thenReturn(observedAreaEntities);
        when(loadSheddingApi.getAreaInfo(anyString())).thenReturn(Single.just(""));

        // Then
        repository.getObservedAreas(value -> fail(), value -> fail());
    }

    @Test
    public void getDaySchedule_whenStatusAndAreaRetrieved_DayScheduleSuccessCallback() {
        // Given
        LoadSheddingRepository loadSheddingRepository = spy(repository);
        final String responseArea = "{\"events\": [{\"end\":\"2022-08-08T22:30:00+02:00\",\"note\":\"Stage 2\",\"start\":\"2022-08-08T20:00:00+02:00\"}],\"info\": {\"name\": \"Sandton-WEST (4)\",\"region\": \"Eskom Direct, City of Johannesburg, Gauteng\"},\"schedule\": {\"days\":[{\"date\":\"2022-08-08\",\"name\": \"Monday\",\"stages\": [[],[\"20:00-22:30\"],[\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-00:30\"],[\"04:00-06:30\",\"12:00-16:30\",\"20:00-00:30\"],[\"04:00-08:30\",\"12:00-16:30\",\"20:00-00:30\"]]}],\"source\":\"https://loadshedding.eskom.co.za/\"}}";
        final String responseStatus = "{\"name\":\"National\",\"next_stages\":[{\"stage\":\"2\",\"stage_start_timestamp\":\"2022-08-08T16:00:00+02:00\"},{\"stage\":\"0\",\"stage_start_timestamp\":\"2022-08-09T00:00:00+02:00\"}],\"stage\":\"1\",\"stage_updated\":\"2022-08-08T16:12:53.725852+02:00\"}";

        // When
        when(loadSheddingApi.getAreaInfo(anyString())).thenReturn(Single.just(responseArea));
        when(loadSheddingApi.getStatus()).thenReturn(Single.just(responseStatus));

        // Then
        loadSheddingRepository.getDaySchedule("id-here", LocalDate.parse("2022-08-08"), value -> {
            assertEquals(value.getOutages().size(), 1);
            assertEquals(value.getOutages().get(0).getStart(), LocalTime.parse("20:00"));
            assertEquals(value.getOutages().get(0).getEnd(), LocalTime.parse("22:30"));
            assertEquals(value.getAreaName(), "Sandton-WEST (4)");
            assertEquals(value.getDowntime(), "Downtime: 2h 30m");
        }, value -> fail());
    }

    @Test
    public void getDaySchedule_whenNoStatusRetrieved_DayScheduleErrorCallback() {
        // Given
        LoadSheddingRepository loadSheddingRepository = spy(repository);
        final String responseArea = "{\"events\": [{\"end\":\"2022-08-08T22:30:00+02:00\",\"note\":\"Stage 2\",\"start\":\"2022-08-08T20:00:00+02:00\"}],\"info\": {\"name\": \"Sandton-WEST (4)\",\"region\": \"Eskom Direct, City of Johannesburg, Gauteng\"},\"schedule\": {\"days\":[{\"date\":\"2022-08-08\",\"name\": \"Monday\",\"stages\": [[],[\"20:00-22:30\"],[\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-22:30\"],[\"04:00-06:30\",\"12:00-14:30\",\"20:00-00:30\"],[\"04:00-06:30\",\"12:00-16:30\",\"20:00-00:30\"],[\"04:00-08:30\",\"12:00-16:30\",\"20:00-00:30\"]]}],\"source\":\"https://loadshedding.eskom.co.za/\"}}";

        // When
        when(loadSheddingApi.getAreaInfo(anyString())).thenReturn(Single.just(responseArea));
        when(loadSheddingApi.getStatus()).thenReturn(Single.error(new Exception()));

        // Then
        loadSheddingRepository.getDaySchedule("id-here", LocalDate.parse("2022-08-08"), value -> fail(), value -> fail());
        verify(loadSheddingApi, times(1)).getAreaInfo(anyString());
    }

    @Test
    public void getDaySchedule_whenNoAreaRetrieved_DayScheduleErrorCallback() {
        // Given
        LoadSheddingRepository loadSheddingRepository = spy(repository);
        final String responseStatus = "{\"name\":\"National\",\"next_stages\":[{\"stage\":\"2\",\"stage_start_timestamp\":\"2022-08-08T16:00:00+02:00\"},{\"stage\":\"0\",\"stage_start_timestamp\":\"2022-08-09T00:00:00+02:00\"}],\"stage\":\"1\",\"stage_updated\":\"2022-08-08T16:12:53.725852+02:00\"}";

        // When
        when(loadSheddingApi.getAreaInfo(anyString())).thenReturn(Single.error(new Exception()));
        when(loadSheddingApi.getStatus()).thenReturn(Single.just(responseStatus));

        // Then
        loadSheddingRepository.getDaySchedule("id-here", LocalDate.parse("2022-08-08"), value -> fail(), value -> fail());
        verify(loadSheddingApi, times(1)).getStatus();
    }
}
