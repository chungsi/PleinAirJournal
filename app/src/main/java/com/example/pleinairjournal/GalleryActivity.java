package com.example.pleinairjournal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends JournalMenu implements View.OnClickListener {

    private GalleryViewModel mGalleryViewModel;
    private GalleryAdapter mAdapter;
    private Button button_testFilter, button_resetFilters;

    private ImageView image_photoPreview;

    long mEntryId;
    int mGalleryAdapterPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = findViewById(R.id.recycler_gallery);
        mAdapter = new GalleryAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEntryId = getIntent().getLongExtra("id", -1);
        mGalleryAdapterPosition = getIntent().getIntExtra("position", -1);

        image_photoPreview = findViewById(R.id.image_photoPreview);


        // Working with the ViewModel, and setting a listener on it to observe data changes
        mGalleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        mGalleryViewModel.getAllEntries().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(List<JournalEntry> journalEntries) {
                Log.i("PLEINAIR_DEBUG", "something in the db has changed.");
                mAdapter.setEntries(journalEntries);
                image_photoPreview.setImageBitmap(journalEntries.getBitmapImage());
            }
        });

        initFilterButtons();

        findMenuButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.onActivityResult(requestCode, resultCode, data);
        image_photoPreview.setImageBitmap(mGalleryViewModel.getBitmapImage());

    }

    /**
     * Initializes the buttons that filter results.
     * OnClickListeners are attached to the buttons as well.
     * */
    private void initFilterButtons() {
        button_testFilter = findViewById(R.id.button_testFilter);
        button_testFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGalleryViewModel.filterByYear("2018");
            }
        });

        // Reset all filters to see all entries in default settings
        button_resetFilters = findViewById(R.id.button_resetFilters);
        button_resetFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGalleryViewModel.refreshEntries();
            }
        });
    }
}
