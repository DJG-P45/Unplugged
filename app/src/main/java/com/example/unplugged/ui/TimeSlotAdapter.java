package com.example.unplugged.ui;

import android.content.Context;
import android.view.ViewGroup;

public interface TimeSlotAdapter <TS extends DailyScheduleBuilder.ViewHolder> {

    TS createView(ViewGroup parent, int position);

    int getItemCount();

}
