package com.example.unplugged.ui.state;

import com.example.unplugged.data.dto.FoundAreaDto;

import java.util.Objects;

public class FoundArea {

    private FoundAreaDto foundAreaDto;
    private Runnable observeArea;

    public FoundArea() {
    }

    public FoundArea(FoundAreaDto foundAreaDto, Runnable observeArea) {
        this.foundAreaDto = foundAreaDto;
        this.observeArea = observeArea;
    }

    public String getTitle() {
        return foundAreaDto.getName();
    }

    public String getSubtitle() {
        return foundAreaDto.getRegion();
    }

    public void observeArea() {
        Objects.requireNonNull(observeArea).run();
    }

    public void setObserveArea(Runnable observeArea) {
        this.observeArea = observeArea;
    }

    public void setFoundAreaDto(FoundAreaDto foundAreaDto) {
        this.foundAreaDto = foundAreaDto;
    }
}
