package com.example.unplugged.data.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StageDto {

    private List<OutageDto> outages;

    public StageDto() {
    }

    @JsonCreator
    public StageDto(List<String> outages) {
        this.outages = new ArrayList<>();

        for (String range : outages) {
            String[] times = range.split("-");
            OutageDto outageDto = new OutageDto(LocalTime.parse(times[0]), LocalTime.parse(times[1]));
            this.outages.add(outageDto);
        }
    }

    public List<OutageDto> getOutages() {
        return outages;
    }

    public void setOutages(List<OutageDto> outages) {
        this.outages = outages;
    }
}
