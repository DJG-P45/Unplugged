package com.example.unplugged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.example.unplugged.ui.DailyScheduleBuilder;
import com.example.unplugged.ui.state.AreaSchedule;
import com.example.unplugged.ui.state.Event;
import com.example.unplugged.ui.HourTimeSlotAdapter;
import com.example.unplugged.ui.LoadSheddingEventAdapter;
import com.example.unplugged.ui.viewmodel.DashboardViewModel;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoadSheddingScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadshedding_schedule);

        DashboardViewModel viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSchedule);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ScrollView scrollView = findViewById(R.id.scrollViewDailySchedule);

        HourTimeSlotAdapter timeSlotAdapter = new HourTimeSlotAdapter();
        LoadSheddingEventAdapter eventAdapter = new LoadSheddingEventAdapter();

        DailyScheduleBuilder scheduleBuilder = new DailyScheduleBuilder(this, scrollView, eventAdapter, timeSlotAdapter);
        scheduleBuilder.build();

        viewModel.getAreaSchedule().observe(this, areaSchedule -> {
            eventAdapter.setEvents(areaSchedule.getEvents());
            scheduleBuilder.build();
            //TODO: Area details should also be displayed
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}