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

            LocalTime start = LocalTime.parse(times[0]);
            LocalTime end = LocalTime.parse(times[1]);

            if (start.isBefore(end)) {
                OutageDto outageDto = new OutageDto(start, end);
                this.outages.add(outageDto);
            }
        }
    }

    public List<OutageDto> getOutages() {
        return outages;
    }

    public void setOutages(List<OutageDto> outages) {
        this.outages = outages;
    }
}
