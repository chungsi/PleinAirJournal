package com.example.pleinairjournal;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class DashboardActivity extends JournalMenu {
    private CompassViewModel mCompassViewModel;
    private SharedPreferences sharedPrefs;
    private GalleryViewModel mGalleryViewModel;
    private GalleryAdapter mAdapter;

    private long mEntryId;
    private int mGalleryAdapterPosition;

    TextView text_name, text_viewCardinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        super.initMenuButtonsWithActive("dashboard");
        super.initCompassDisplay();

        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String username = sharedPrefs.getString("USERNAME", "");
        text_name = findViewById(R.id.text_name);
        text_name.setText(username + "!");

//        text_viewCardinal = findViewById(R.id.text_compass);
        text_viewCardinal = super.getCompassTextView();

        // Compass view model to display and update the cardinal direction view
        mCompassViewModel = ViewModelProviders.of(this).get(CompassViewModel.class);
        mCompassViewModel.compassLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                text_viewCardinal.setText(s);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_gallery);
        mAdapter = new GalleryAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

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
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

}
