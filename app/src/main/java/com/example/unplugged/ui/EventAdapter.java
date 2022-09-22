package com.example.unplugged.ui;

import android.view.ViewGroup;

public interface EventAdapter <EV extends DailyScheduleBuilder.ViewHolder> {

    EV createView(ViewGroup parent, int position);

    int getItemCount();

}
