package com.example.unplugged.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unplugged.data.other.ErrorCategory;

public abstract class BaseViewModel extends AndroidViewModel {

    private LiveData<Consumable<ErrorCategory>> errorFeed;
    private MutableLiveData<String> uiErrorFeed;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        errorFeed = new MutableLiveData<>();
    }

    protected void initErrorFeed(LiveData<Consumable<ErrorCategory>> errorFeed) {
        this.errorFeed = errorFeed;
    }

    public LiveData<String> getUiErrorFeed() {
        uiErrorFeed = new MutableLiveData<>();
        errorFeed.observeForever(consumable -> {
            if (consumable.notConsumed()) {
                switch (consumable.consume()) {
                    case NETWORK: uiErrorFeed.setValue("Network Error"); break;
                    case SYSTEM: uiErrorFeed.setValue("System Error"); break;
                    default: uiErrorFeed.setValue("Unknown Error"); break;
                }
            }
        });

        return uiErrorFeed;
    }
}
