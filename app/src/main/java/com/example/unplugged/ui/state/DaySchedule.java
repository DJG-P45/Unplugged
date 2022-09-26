package com.example.unplugged.ui.state;

import com.example.unplugged.data.dto.DayScheduleDto;

import java.util.List;
import java.util.Objects;

public class DaySchedule {

    private DayScheduleDto schedule;
    private Runnable todaySchedule;
    private Runnable previousDaySchedule;
    private Runnable nextDaySchedule;

    public DayScheduleDto getSchedule() {
        return schedule;
    }

    public void setSchedule(DayScheduleDto schedule) {
        this.schedule = schedule;
    }

    public void loadTodaySchedule() {
        todaySchedule.run();
    }

    public void setTodaySchedule(Runnable todaySchedule) {
        this.todaySchedule = todaySchedule;
    }

    public void loadPreviousDaySchedule() {
        previousDaySchedule.run();
    }

    public void setPreviousDaySchedule(Runnable previousDaySchedule) {
        this.previousDaySchedule = previousDaySchedule;
    }

    public void loadNextDaySchedule() {
        nextDaySchedule.run();
    }

    public void setNextDaySchedule(Runnable nextDaySchedule) {
        this.nextDaySchedule = nextDaySchedule;
    }
}
