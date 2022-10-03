package com.example.unplugged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unplugged.ui.DailyScheduleBuilder;
import com.example.unplugged.ui.HourlyScheduleAdapter;
import com.example.unplugged.ui.OutageAdapter;
import com.example.unplugged.ui.viewmodel.DayScheduleViewModel;

import java.util.Objects;

public class DailyScheduleActivity extends AppCompatActivity {

    private TextView txtDate, txtAreaName, txtDownTime;
    private ImageButton btnNextDay, btnPreviousDay;
    private Toolbar toolbar;
    private ScrollView scrollViewSchedule;
    private DayScheduleViewModel dayScheduleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_schedule);

        dayScheduleViewModel = new ViewModelProvider(this).get(DayScheduleViewModel.class);

        // Retrieve ui views
        toolbar = (Toolbar) findViewById(R.id.toolbarSchedule);
        txtDate = findViewById(R.id.txtScheduleDate);
        txtAreaName = findViewById(R.id.txtScheduleAreaName);
        txtDownTime = findViewById(R.id.txtScheduleTotalDowntime);
        btnNextDay = findViewById(R.id.btnScheduleNext);
        btnPreviousDay = findViewById(R.id.btnSchedulePrevious);
        scrollViewSchedule = findViewById(R.id.scrollViewDailySchedule);
        ProgressBar loadingBar = findViewById(R.id.scheduleLoadingBar);

        // Enable toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Init hourly schedule view
        HourlyScheduleAdapter timeSlotAdapter = new HourlyScheduleAdapter();
        OutageAdapter outageAdapter = new OutageAdapter();
        DailyScheduleBuilder scheduleBuilder = new DailyScheduleBuilder(this, scrollViewSchedule, outageAdapter, timeSlotAdapter);
        scheduleBuilder.build();

        // Get ID of area so its schedule can be retrieved
        String areaId = "";
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Toast.makeText(this, "Could not determine area!", Toast.LENGTH_SHORT).show();
        } else {
            areaId = extras.getString("AREA_ID");
        }

        // When daily outage schedule present update ui views
        dayScheduleViewModel.getDaySchedule(areaId).observe(this, daySchedule -> {
            outageAdapter.setOutages(daySchedule.getSchedule().getOutages());

            txtDate.setText(daySchedule.getSchedule().getDate());
            txtAreaName.setText(daySchedule.getSchedule().getAreaName());
            txtDownTime.setText(daySchedule.getSchedule().getDowntime());

            btnNextDay.setOnClickListener(view -> daySchedule.loadNextDaySchedule());
            btnPreviousDay.setOnClickListener(view -> daySchedule.loadPreviousDaySchedule());

            loadingBar.setVisibility(View.INVISIBLE);
        });

        // Display any possible error that might arise from the data layer
        dayScheduleViewModel.getUiErrorFeed().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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