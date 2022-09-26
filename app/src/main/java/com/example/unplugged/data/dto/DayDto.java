package com.example.unplugged.data.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDate;
import java.util.List;

public class DayDto {

    private LocalDate date;
    private String name;
    private List<StageDto> stages;

    public DayDto() {
    }

    public DayDto(LocalDate date, String name, List<StageDto> stages) {
        this.date = date;
        this.name = name;
        this.stages = stages;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StageDto> getStages() {
        return stages;
    }

    public void setStages(List<StageDto> stages) {
        this.stages = stages;
    }
}
