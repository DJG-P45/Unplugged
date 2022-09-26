package com.example.unplugged.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.unplugged.R;
import com.example.unplugged.data.dto.OutageDto;

import java.util.ArrayList;
import java.util.List;

public class LoadSheddingEventAdapter implements EventAdapter <LoadSheddingEventAdapter.EventViewHolder> {

    private List<OutageDto> outages;

    public LoadSheddingEventAdapter() {
        this.outages = new ArrayList<>();
    }


    @Override
    public EventViewHolder createView(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(view);

        float dp = parent.getResources().getDisplayMetrics().density;
        float sp = parent.getResources().getDisplayMetrics().scaledDensity;

        OutageDto outage = outages.get(position);

        int start = outage.getStart().getHour() * 60;
        start += outage.getStart().getMinute();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(250 * sp),(int)(outage.getDurationInMinutes() * dp));
        params.topMargin = (int)(start * dp);
        params.leftMargin = (int)(60 * dp);
        viewHolder.layout.setLayoutParams(params);

        viewHolder.label.setText(outage.getStart() + "-" + outage.getEnd());

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return outages.size();
    }

    public void setOutages(List<OutageDto> outages) {
        this.outages = outages;
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
