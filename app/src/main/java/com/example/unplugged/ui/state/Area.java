package com.example.unplugged.ui.state;

import java.util.Objects;

public class Area {

    private String id;
    private String name;
    private String region;
    private String event;
    private Runnable removeArea;

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

    public void removeArea() {
        Objects.requireNonNull(removeArea).run();
    }

    public void setRemoveArea(Runnable removeArea) {
        this.removeArea = removeArea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
