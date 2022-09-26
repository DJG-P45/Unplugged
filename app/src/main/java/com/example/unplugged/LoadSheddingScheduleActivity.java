package com.example.unplugged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.unplugged.ui.DailyScheduleBuilder;
import com.example.unplugged.ui.HourTimeSlotAdapter;
import com.example.unplugged.ui.LoadSheddingEventAdapter;
import com.example.unplugged.ui.viewmodel.DayScheduleViewModel;

import java.util.Objects;

public class LoadSheddingScheduleActivity extends AppCompatActivity {

    private TextView txtDate, txtAreaName, txtDownTime, txtNextDay, txtPeviousDay;
    private ImageButton btnNextDay, btnPreviousDay;
    private Toolbar toolbar;
    private ScrollView scrollViewSchedule;
    private DayScheduleViewModel dayScheduleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadshedding_schedule);

        toolbar = (Toolbar) findViewById(R.id.toolbarSchedule);
        txtDate = findViewById(R.id.txtScheduleDate);
        txtAreaName = findViewById(R.id.txtScheduleAreaName);
        txtDownTime = findViewById(R.id.txtScheduleTotalDowntime);
        btnNextDay = findViewById(R.id.btnScheduleNext);
        txtNextDay = findViewById(R.id.txtScheduleNext);
        btnPreviousDay = findViewById(R.id.btnSchedulePrevious);
        txtPeviousDay = findViewById(R.id.txtSchedulePrevious);
        scrollViewSchedule = findViewById(R.id.scrollViewDailySchedule);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        dayScheduleViewModel = new ViewModelProvider(this).get(DayScheduleViewModel.class);

        HourTimeSlotAdapter timeSlotAdapter = new HourTimeSlotAdapter();
        LoadSheddingEventAdapter eventAdapter = new LoadSheddingEventAdapter();
        DailyScheduleBuilder scheduleBuilder = new DailyScheduleBuilder(this, scrollViewSchedule, eventAdapter, timeSlotAdapter);
        scheduleBuilder.build();

        dayScheduleViewModel.getDaySchedule("").observe(this, daySchedule -> {
            eventAdapter.setOutages(daySchedule.getSchedule().getOutages());
            scheduleBuilder.build();

            txtDate.setText(daySchedule.getSchedule().getDate());
            txtAreaName.setText(daySchedule.getSchedule().getAreaName());
            txtDownTime.setText(daySchedule.getSchedule().getDowntime());

            btnNextDay.setOnClickListener(view -> daySchedule.loadNextDaySchedule());
            btnPreviousDay.setOnClickListener(view -> daySchedule.loadPreviousDaySchedule());
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