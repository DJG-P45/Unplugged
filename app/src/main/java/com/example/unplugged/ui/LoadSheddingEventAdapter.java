package com.example.unplugged.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.unplugged.R;
import com.example.unplugged.ui.state.Event;

import java.util.ArrayList;
import java.util.List;

public class LoadSheddingEventAdapter implements EventAdapter <LoadSheddingEventAdapter.EventViewHolder> {

    private List<Event> events;

    public LoadSheddingEventAdapter() {
        this.events = new ArrayList<>();
    }


    @Override
    public EventViewHolder createView(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(view);

        float dp = parent.getResources().getDisplayMetrics().density;
        float sp = parent.getResources().getDisplayMetrics().scaledDensity;

        Event event = events.get(position);

        int start = event.getStartTime().getHour() * 60;
        start += event.getStartTime().getMinute();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(200 * sp),(int)(event.getDurationInMinutes() * dp));
        params.topMargin = (int)(start * dp);
        params.leftMargin = (int)(60 * dp);
        viewHolder.layout.setLayoutParams(params);

        viewHolder.label.setText(event.getNote());

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    protected class EventViewHolder extends DailyScheduleBuilder.ViewHolder {

        public LinearLayout layout;
        public TextView label;

        public EventViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.linearLayoutEvent);
            label = view.findViewById(R.id.txtEventLabel);
        }
    }
}
