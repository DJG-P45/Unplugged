package com.example.unplugged.data.dto;

import java.time.ZonedDateTime;

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
}
