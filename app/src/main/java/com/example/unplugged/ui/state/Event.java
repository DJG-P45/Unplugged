package com.example.unplugged.ui.state;

import java.time.LocalTime;

public class Event {

    private LocalTime startTime;
    private int durationInMinutes;
    private String note;

    public Event() {
    }

    public Event(LocalTime startTime, int durationInMinutes, String note) {
        this.startTime = startTime;
        this.durationInMinutes = durationInMinutes;
        this.note = note;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
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
