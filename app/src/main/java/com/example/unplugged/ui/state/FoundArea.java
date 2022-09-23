package com.example.unplugged.ui.state;

import java.util.Objects;

public class FoundArea {

    private String title;
    private String subtitle;
    private Runnable observeArea;

    public FoundArea() {
    }

    public FoundArea(String title, String subtitle, Runnable observeArea) {
        this.title = title;
        this.subtitle = subtitle;
        this.observeArea = observeArea;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void observeArea() {
        Objects.requireNonNull(observeArea).run();
    }

    public void setObserveArea(Runnable observeArea) {
        this.observeArea = observeArea;
    }
}
