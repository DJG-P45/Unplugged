package com.example.unplugged.ui;

import android.view.ViewGroup;

public abstract class DailyScheduleAdapter {

    private DailyScheduleBuilder dailyScheduleBuilder;

    public void setBuilder(DailyScheduleBuilder dailyScheduleBuilder) {
        this.dailyScheduleBuilder = dailyScheduleBuilder;
    }

    protected void notifyDataSetChanged() {
        dailyScheduleBuilder.build();
    }

    public abstract DailyScheduleBuilder.ViewHolder createView(ViewGroup parent, int position);

    public abstract int getItemCount();


}
