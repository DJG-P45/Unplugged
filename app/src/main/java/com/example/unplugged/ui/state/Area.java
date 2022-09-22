package com.example.unplugged.ui.state;

import android.view.View;

public class Area {

    private String name;
    private String region;
    private String event;
    private Runnable selectAction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Runnable getSelectAction() {
        return selectAction;
    }

    public void setSelectAction(Runnable selectAction) {
        this.selectAction = selectAction;
    }
}
