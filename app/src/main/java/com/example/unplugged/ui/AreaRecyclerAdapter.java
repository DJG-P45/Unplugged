package com.example.unplugged.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unplugged.DailyScheduleActivity;
import com.example.unplugged.R;
import com.example.unplugged.ui.state.Area;

import java.util.ArrayList;
import java.util.List;

public class AreaRecyclerAdapter extends RecyclerView.Adapter <AreaRecyclerAdapter.AreaViewHolder> {

    private List<Area> areas;
    private Context context;

    public AreaRecyclerAdapter() {
        this.areas = new ArrayList<>();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areas.get(position);
        holder.name.setText(area.getName());
        holder.region.setText(area.getRegion());
        holder.event.setText(area.getEvent());
        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(context, DailyScheduleActivity.class);
            intent.putExtra("AREA_ID", area.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        Area area = areas.get(position);
        area.removeArea();
        areas.remove(position);
        notifyItemRemoved(position);
    }

    protected static class AreaViewHolder extends RecyclerView.ViewHolder {

        public TextView name, region, event;
        public CardView card;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemAreaTitle);
            region = itemView.findViewById(R.id.itemAreaSubtitle);
            event = itemView.findViewById(R.id.itemAreaDetails);
            card = itemView.findViewById(R.id.itemAreaCard);
        }
    }

}
