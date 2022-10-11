package com.example.unplugged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.unplugged.ui.FoundAreasRecyclerAdapter;
import com.example.unplugged.ui.viewmodel.AddAreaViewModel;

import java.util.Objects;

public class AddAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area);

        AddAreaViewModel addAreaViewModel = new ViewModelProvider(this).get(AddAreaViewModel.class);

        // Retrieve ui views
        Toolbar toolbar = findViewById(R.id.toolbarAddArea);
        RecyclerView recyclerFoundAreas = findViewById(R.id.recyclerFoundAreas);
        EditText edtTxtAreaSearch = findViewById(R.id.edtTxtAreaSearch);
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        RelativeLayout progressBarContainer = findViewById(R.id.progressBarContainer);

        // Enable toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Init recycler view that displays areas match search text
        FoundAreasRecyclerAdapter adapter = new FoundAreasRecyclerAdapter();
        recyclerFoundAreas.setAdapter(adapter);
        recyclerFoundAreas.setLayoutManager(new LinearLayoutManager(this));

        // Display any possible error that might arise from the data layer
        addAreaViewModel.getUiErrorFeed().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        // If user types in search text and presses btn query data layer for possible suggestions matching user input
        btnSearch.setOnClickListener(view -> {
            addAreaViewModel.findAreas(edtTxtAreaSearch.getText().toString()).observe(AddAreaActivity.this, foundAreas -> {
                adapter.setFoundAreas(foundAreas);
                progressBarContainer.setVisibility(View.INVISIBLE);
            });
            progressBarContainer.setVisibility(View.VISIBLE);
        });

        // Listen for newly added area
        addAreaViewModel.isAreaAdded().observe(this, added -> {
            if (added) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
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