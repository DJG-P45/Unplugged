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
import android.widget.EditText;
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

        // If user types in search edit text query data layer for possible suggestions matching user input
        edtTxtAreaSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                addAreaViewModel.findAreas(editable.toString()).observe(AddAreaActivity.this, adapter::setFoundAreas);
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