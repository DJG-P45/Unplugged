package com.example.unplugged.data.dto;

import com.example.unplugged.data.other.PrettyTime;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class StatusDto {

    private int stage;
    private ZonedDateTime updated;

    public StatusDto() {
    }

    public StatusDto(int stage, ZonedDateTime updated) {
        this.stage = stage;
        this.updated = updated;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    @JsonSetter("stage_updated")
    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getElapsedTime() {
        long timeSince = updated.until(ZonedDateTime.now(), ChronoUnit.MINUTES);
        Duration duration = Duration.ofMinutes(timeSince);
        return  ("Since " + PrettyTime.beautify(duration) + " ago");
    }
}
