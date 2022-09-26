package com.example.unplugged.data.dto;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class OutageDto {

    private LocalTime start;
    private LocalTime end;

    public OutageDto() {
    }

    public OutageDto(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
    public long getDurationInMinutes() {
        if (start != null && end != null) {
            return start.until(end, ChronoUnit.MINUTES);
        }
        return 0;
    }

}
