package com.example.unplugged.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.unplugged.R;

public class HourTimeSlotAdapter implements TimeSlotAdapter <HourTimeSlotAdapter.TimeSlotViewHolder> {

    @Override
    public TimeSlotViewHolder createView(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        TimeSlotViewHolder viewHolder = new TimeSlotViewHolder(view);
        viewHolder.timeLabel.setText(String.valueOf(position));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return 25;
    }

    protected class TimeSlotViewHolder extends DailyScheduleBuilder.ViewHolder {

        public TextView timeLabel;

        public TimeSlotViewHolder(View view) {
            super(view);
            timeLabel = view.findViewById(R.id.txtTimeSlotTime);
        }
    }
}
