package com.example.unplugged.ui.state;

import java.util.List;

public class AreaSchedule {

    private Area area;
    private List<Event> events;

    public AreaSchedule() {
    }

    public AreaSchedule(Area area, List<Event> events) {
        this.area = area;
        this.events = events;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
