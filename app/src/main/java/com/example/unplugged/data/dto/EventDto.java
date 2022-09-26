package com.example.unplugged.data.dto;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class EventDto {

    private ZonedDateTime start;
    private ZonedDateTime end;
    private String note;

    public EventDto() {
    }

    public EventDto(ZonedDateTime start, ZonedDateTime end, String note) {
        this.start = start;
        this.end = end;
        this.note = note;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @NonNull
    @Override
    public String toString() {
        return note + ": " + start.toLocalTime() + " - " + end.toLocalTime();
    }
}
