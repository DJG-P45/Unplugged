package com.example.unplugged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unplugged.ui.AreaRecyclerAdapter;
import com.example.unplugged.ui.RemoveAreaCallback;
import com.example.unplugged.ui.viewmodel.AreasViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView txtStatusTitle, txtStatusSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AreasViewModel viewModel = new ViewModelProvider(this).get(AreasViewModel.class);

        // Enable custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDashboard);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Set up and display LoadShedding status
        txtStatusTitle = findViewById(R.id.txtDashboardTitle);
        txtStatusSubtitle = findViewById(R.id.txtDashboardSubtitle);

        viewModel.getStatus().observe(this, uiStatus -> {
            txtStatusTitle.setText(uiStatus.getStage());
            txtStatusSubtitle.setText(uiStatus.getUpdated());
        });

        // Set up and display observed areas
        AreaRecyclerAdapter adapter = new AreaRecyclerAdapter();
        RecyclerView recyclerView = findViewById(R.id.recyclerAreas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the ability to remove observed areas by swiping left
        RemoveAreaCallback removeAreaCallback = new RemoveAreaCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(removeAreaCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Get observed areas and populate recycler view
        viewModel.getObservedAreas().observe(this, adapter::setAreas);

        // Display any possible error that might arise from the data layer
        viewModel.getUiErrorFeed().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle arrow click here
        if (item.getItemId() == R.id.menuItemAdd) {
            Intent intent = new Intent(this, AddAreaActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return true;
    }
}