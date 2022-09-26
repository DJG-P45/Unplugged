package com.example.unplugged.data.dto;

import java.util.List;

public class DayScheduleDto {

    private String areaName;
    private String date;
    private String downtime;
    private List<OutageDto> outages;

    public DayScheduleDto() {
    }

    public DayScheduleDto(String areaName, String date, String downtime, List<OutageDto> outages) {
        this.areaName = areaName;
        this.date = date;
        this.downtime = downtime;
        this.outages = outages;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDowntime() {
        return downtime;
    }

    public void setDowntime(String downtime) {
        this.downtime = downtime;
    }

    public List<OutageDto> getOutages() {
        return outages;
    }

    public void setOutages(List<OutageDto> outages) {
        this.outages = outages;
    }
}
