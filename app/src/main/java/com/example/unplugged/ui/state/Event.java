package com.example.unplugged.ui.state;

import java.time.ZonedDateTime;

public class Event {

    private ZonedDateTime startTime;
    private int durationInMinutes;
    private String note;

    public Event() {
    }

    public Event(ZonedDateTime startTime, int durationInMinutes, String note) {
        this.startTime = startTime;
        this.durationInMinutes = durationInMinutes;
        this.note = note;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
