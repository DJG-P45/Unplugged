package com.example.unplugged.data.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.unplugged.data.datasource.EskomSePushNetworkApi;
import com.example.unplugged.data.datasource.Callback;
import com.example.unplugged.data.datasource.LoadSheddingApi;
import com.example.unplugged.data.datasource.ObservedAreaDao;
import com.example.unplugged.data.datasource.UnpluggedDatabase;
import com.example.unplugged.data.datasource.VolleyRequestManager;
import com.example.unplugged.data.dto.StatusDto;
import com.example.unplugged.repository.LoadSheddingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.TestScheduler;

@RunWith(MockitoJUnitRunner.class)
public class LoadSheddingRepositoryTests {

    @Mock
    private ObservedAreaDao observedAreaDao;

    @Mock
    private LoadSheddingApi loadSheddingApi;

    @Spy
    private ObjectMapper mapper;

    @InjectMocks
    LoadSheddingRepository repository = new LoadSheddingRepository(loadSheddingApi, observedAreaDao);

    @Mock
    private Schedulers schedulers;

    @Mock
    private AndroidSchedulers androidSchedulers;

    @Test
    public void loadSheddingRepository_getStatus_returnsStatus() {


    }
}
