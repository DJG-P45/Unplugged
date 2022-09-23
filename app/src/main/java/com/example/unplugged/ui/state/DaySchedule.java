package com.example.unplugged.ui.state;

import java.util.List;
import java.util.Objects;

public class DaySchedule {

    private String areaName;
    private String currentDate;
    private String previousDate;
    private String nextDate;
    private String downtime;
    private List<Event> events;
    private Runnable todaySchedule;
    private Runnable previousDaySchedule;
    private Runnable nextDaySchedule;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getDowntime() {
        return downtime;
    }

    public void setDowntime(String downtime) {
        this.downtime = downtime;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setTodaySchedule(Runnable todaySchedule) {
        this.todaySchedule = todaySchedule;
    }

    public void setPreviousDaySchedule(Runnable previousDaySchedule) {
        this.previousDaySchedule = previousDaySchedule;
    }

    public void setNextDaySchedule(Runnable nextDaySchedule) {
        this.nextDaySchedule = nextDaySchedule;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public void loadNextDaySchedule() {
        Objects.requireNonNull(nextDaySchedule).run();
    }

    public void loadPreviousDaySchedule() {
        Objects.requireNonNull(previousDaySchedule).run();
    }

    public void loadTodaySchedule() {
        Objects.requireNonNull(todaySchedule).run();
    }
}
