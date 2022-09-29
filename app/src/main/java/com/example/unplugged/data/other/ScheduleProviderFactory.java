package com.example.unplugged.data.other;

public class ScheduleProviderFactory {

    public static SchedulerProvider getScheduleProvider() {
        return new DefaultSchedulerProvider();
    }

}
