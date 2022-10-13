package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.other.ErrorCategory;
import com.example.unplugged.data.repository.ICallback;

public abstract class BaseViewModel extends AndroidViewModel {

    private MutableLiveData<String> uiErrorFeed;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    protected void initErrorFeed(LiveData<Consumable<ErrorCategory>> errorFeed) {
        //TODO Remove this method
    }

    public LiveData<String> getUiErrorFeed() {
        uiErrorFeed = new MutableLiveData<>();
        return uiErrorFeed;
    }

    protected ICallback<ErrorCategory> errorCallback() {
        return errorCategory -> {
            //uiErrorFeed = new MutableLiveData<>();
            switch (errorCategory) {
                case NETWORK: uiErrorFeed.setValue("Network Error"); break;
                case SERVICE: uiErrorFeed.setValue("System Error"); break;
                case FAILED_TO_FETCH_AREA: uiErrorFeed.setValue("Could not fetch area"); break;
                case FAILED_TO_FETCH_AREAS: uiErrorFeed.setValue("Could not fetch areas"); break;
                case FAILED_TO_FETCH_STATUS: uiErrorFeed.setValue("Could not fetch status"); break;
                case FAILED_TO_FETCH_SCHEDULE: uiErrorFeed.setValue("Could not fetch schedule"); break;
                default: uiErrorFeed.setValue("Unknown Error"); break;
            }
        };
    }
}
