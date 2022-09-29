package com.example.unplugged.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.unplugged.data.datasource.LoadSheddingApi;
import com.example.unplugged.data.datasource.ObservedAreaDao;
import com.example.unplugged.data.dto.FoundAreaDto;
import com.example.unplugged.data.dto.StatusDto;
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


}
