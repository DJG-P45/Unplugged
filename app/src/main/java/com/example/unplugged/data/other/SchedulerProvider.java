package com.example.unplugged.data.other;

import io.reactivex.rxjava3.core.Scheduler;

public interface SchedulerProvider {
    Scheduler newThread();
    Scheduler mainThread();
}
