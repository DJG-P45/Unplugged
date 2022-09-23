package com.example.unplugged.data.dto;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class StatusDto {

    private String stage;
    private ZonedDateTime updated;

    public StatusDto() {
    }

    public StatusDto(String stage, ZonedDateTime updated) {
        this.stage = stage;
        this.updated = updated;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getElapsedTime() {
        // TODO: Maybe there should be check to see if updated date is before current date?
        long timeSince = updated.until(ZonedDateTime.now(), ChronoUnit.HOURS);
        return  ("Since " + timeSince + " hours ago");
    }
}
