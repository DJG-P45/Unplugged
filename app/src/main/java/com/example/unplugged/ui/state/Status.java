package com.example.unplugged.ui.state;

public class Status {

    private String stage;
    private String updated;

    public Status() {
    }

    public Status(String stage, String updated) {
        this.stage = stage;
        this.updated = updated;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
