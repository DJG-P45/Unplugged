package com.example.unplugged.data.dto;

public class AreaDto {

    private String id;
    private String name;
    private String region;
    private String event;

    public AreaDto() {
    }


    public AreaDto(String id, String name, String region, String event) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
