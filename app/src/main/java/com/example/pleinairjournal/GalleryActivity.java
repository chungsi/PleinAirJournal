package com.example.pleinairjournal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends JournalMenu implements View.OnClickListener {

    private GalleryViewModel mGalleryViewModel;
    private GalleryAdapter mAdapter;
    private Button button_resetFilters, button_applyFilters;
    private Spinner spinner_year, spinner_month, spinner_location;

    long mEntryId;
    int mGalleryAdapterPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = findViewById(R.id.recycler_gallery);
        mAdapter = new GalleryAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mEntryId = getIntent().getLongExtra("id", -1);
        mGalleryAdapterPosition = getIntent().getIntExtra("position", -1);

        // Working with the ViewModel, and setting a listener on it to observe data changes
        mGalleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        mGalleryViewModel.getAllEntries().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(List<JournalEntry> journalEntries) {
                Log.i("PLEINAIR_DEBUG", "something in the db has changed.");
                mAdapter.setEntries(journalEntries);
            }
        });

        initFilters();

        findMenuButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Initializes the buttons that filter results.
     * OnClickListeners are attached to the buttons as well.
     * */
    private void initFilters() {
        initFilterSpinners();

        button_applyFilters = findViewById(R.id.button_applyFilters);
        button_applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGalleryViewModel.filterBy(
                        spinner_year.getSelectedItem().toString(),
                        spinner_month.getSelectedItem().toString(),
                        spinner_location.getSelectedItem().toString());
            }
        });

        // Reset all filters to see all entries in default settings
        button_resetFilters = findViewById(R.id.button_resetFilters);
        button_resetFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGalleryViewModel.refreshEntries();
                spinner_year.setSelection(0, true);
                spinner_month.setSelection(0, true);
                spinner_location.setSelection(0, true);
            }
        });
    }

    private void initFilterSpinners() {
        spinner_year = findViewById(R.id.spinner_year);
        spinner_month = findViewById(R.id.spinner_month);
        spinner_location = findViewById(R.id.spinner_location);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.year_array,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(arrayAdapter);

        arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.month_array,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(arrayAdapter);

        // To get all locations, need to call the viewModel's function to get all locations from db.
        List<String> locationsList = mGalleryViewModel.getAllLocations();
        ArrayAdapter<String> locationArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                locationsList);
        spinner_location.setAdapter(locationArrayAdapter);
    }
}
