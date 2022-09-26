package com.example.unplugged.data.dto;

import java.util.List;

public class ScheduleDto {

    private List<DayDto> days;

    public ScheduleDto() {
    }

    public ScheduleDto(List<DayDto> days) {
        this.days = days;
    }

    public List<DayDto> getDays() {
        return days;
    }

    public void setDays(List<DayDto> days) {
        this.days = days;
    }
}
