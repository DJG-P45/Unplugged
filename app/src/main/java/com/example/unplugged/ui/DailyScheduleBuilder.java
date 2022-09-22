package com.example.unplugged.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class DailyScheduleBuilder {

    private Context context;
    private final ScrollView scrollView;
    private final EventAdapter eventAdapter;
    private final TimeSlotAdapter timeSlotAdapter;

    public DailyScheduleBuilder(Context context, ScrollView scrollView, EventAdapter eventAdapter, TimeSlotAdapter timeSlotAdapter) {
        this.context = context;
        this.scrollView = scrollView;
        this.eventAdapter = eventAdapter;
        this.timeSlotAdapter = timeSlotAdapter;
    }

    public void build() {
        // Clear the canvas
        scrollView.removeAllViews();

        // Create time slots
        LinearLayout linearLayout = createLinearLayout();

        for (int s = 0; s < timeSlotAdapter.getItemCount(); s++) {
            ViewHolder timeSlot = timeSlotAdapter.createView(linearLayout, s);
            linearLayout.addView(timeSlot.getView());
        }

        // Create events
        RelativeLayout relativeLayout = createRelativeLayout();
        relativeLayout.addView(linearLayout);

        for (int e = 0; e < eventAdapter.getItemCount(); e++) {
            ViewHolder event = eventAdapter.createView(relativeLayout, e);
            relativeLayout.addView(event.getView());
        }

        scrollView.addView(relativeLayout);
    }

    private RelativeLayout createRelativeLayout() {
        RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 0; // Must be flush with top of ScrollView
        layout.setLayoutParams(params);
        return layout;
    }

    private LinearLayout createLinearLayout() {
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 0; // Must be flush with top of RelativeLayout
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    public abstract static class ViewHolder {

        private final View view;

        public ViewHolder(View view) {
            this.view = view;
        }

        public View getView() {
            return view;
        }
    }
}
