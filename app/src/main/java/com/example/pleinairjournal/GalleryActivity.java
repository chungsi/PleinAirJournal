package com.example.pleinairjournal;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Gallery;
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

public class GalleryActivity extends AppCompatActivity {

    private GalleryViewModel mGalleryViewModel;
    private GalleryAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = findViewById(R.id.recycler_gallery);
        mAdapter = new GalleryAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
         * Working with the ViewModel, and setting a listener on it to observe data changes
         * */
        mGalleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        mGalleryViewModel.getAllEntries().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(List<JournalEntry> journalEntries) {
                Log.i("PLEINAIR_DEBUG", "something in the db has changed.");
                mAdapter.setEntries(journalEntries);
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAdapter.notifyDataSetChanged(); // TEMP
//    }
}
