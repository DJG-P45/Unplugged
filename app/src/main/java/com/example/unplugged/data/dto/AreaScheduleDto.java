package com.example.unplugged.data.dto;

import java.util.List;

public class AreaScheduleDto {

    private AreaDto area;
    private List<EventDto> events;

    public AreaScheduleDto() {
    }

    public AreaScheduleDto(AreaDto area, List<EventDto> events) {
        this.area = area;
        this.events = events;
    }

    public AreaDto getArea() {
        return area;
    }

    public void setArea(AreaDto area) {
        this.area = area;
    }

    public List<EventDto> getEvents() {
        return events;
    }

    public void setEvents(List<EventDto> events) {
        this.events = events;
    }
}
