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

import com.example.unplugged.MainActivity;
import com.example.unplugged.R;
import com.example.unplugged.ui.state.FoundArea;

import java.util.ArrayList;
import java.util.List;

public class FoundAreasRecyclerAdapter extends RecyclerView.Adapter <FoundAreasRecyclerAdapter.FoundAreaViewHolder> {

    private List<FoundArea> foundAreas;
    private Context context;

    public FoundAreasRecyclerAdapter() {
        this.foundAreas = new ArrayList<>();
    }

    @NonNull
    @Override
    public FoundAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_found_area, parent, false);
        return new FoundAreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoundAreaViewHolder holder, int position) {
        FoundArea area = foundAreas.get(position);
        holder.title.setText(area.getTitle());
        holder.subtitle.setText(area.getSubtitle());
        holder.card.setOnClickListener(view -> {
            area.observeArea();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foundAreas.size();
    }

    public void setFoundAreas(List<FoundArea> foundAreas) {
        this.foundAreas = foundAreas;
        notifyDataSetChanged();
    }

    protected class FoundAreaViewHolder extends RecyclerView.ViewHolder {

        public TextView title, subtitle;
        public CardView card;

        public FoundAreaViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtFoundAreaTitle);
            subtitle = itemView.findViewById(R.id.txtFoundAreaSubtitle);
            card = itemView.findViewById(R.id.cardFoundAreaItem);
        }
    }
}
