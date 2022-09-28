package com.example.unplugged.ui.state;

import com.example.unplugged.data.dto.AreaDto;

import java.util.Objects;

public class Area {

    private AreaDto areaDto;
    private Runnable removeArea;

    public Area() {
    }

    public Area(AreaDto areaDto, Runnable removeArea) {
        this.areaDto = areaDto;
        this.removeArea = removeArea;
    }

    public AreaDto getAreaDto() {
        return areaDto;
    }

    public void setAreaDto(AreaDto areaDto) {
        this.areaDto = areaDto;
    }

    public String getId() {
        return areaDto.getId();
    }

    public String getName() {
        return areaDto.getInfo().getName();
    }

    public String getRegion() {
        return areaDto.getInfo().getRegion();
    }

    public String getEvent() {
        if (areaDto.getEvents().size() > 0) {
            return areaDto.getEvents().get(0).toString();
        }
        return "No outages expected";
    }

    public void removeArea() {
        Objects.requireNonNull(removeArea).run();
    }

    public void setRemoveArea(Runnable removeArea) {
        this.removeArea = removeArea;
    }

}
