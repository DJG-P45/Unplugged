package com.example.unplugged.data.dto;

import java.util.List;

public class AreaDto {

    private String id;
    private InfoDto info;
    private List<EventDto> events;
    private ScheduleDto schedule;


    public AreaDto() {
    }

    public AreaDto(String id, InfoDto info, List<EventDto> events, ScheduleDto schedule) {
        this.id = id;
        this.info = info;
        this.events = events;
        this.schedule = schedule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InfoDto getInfo() {
        return info;
    }

    public void setInfo(InfoDto info) {
        this.info = info;
    }

    public List<EventDto> getEvents() {
        return events;
    }

    public void setEvents(List<EventDto> events) {
        this.events = events;
    }

    public ScheduleDto getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleDto schedule) {
        this.schedule = schedule;
    }
}
