package com.example.pleinairjournal;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class DashboardActivity extends JournalMenu
        implements FilterDialogFragment.FilterDialogListener {
    private CompassViewModel mCompassViewModel;
    private SharedPreferences sharedPrefs;
    private GalleryViewModel mGalleryViewModel;
    private GalleryAdapter mAdapter;

//    private long mEntryId;
//    private int mGalleryAdapterPosition;

    TextView text_name, text_viewCardinal;
    Button button_openFilterDialog;

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

        initFilterButton();
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

//        mEntryId = getIntent().getLongExtra("id", -1);
//        mGalleryAdapterPosition = getIntent().getIntExtra("position", -1);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* When an Intent is received, the message is passed to the adapter to handle any updating
        of entries */
        mAdapter.onActivityResult(requestCode, resultCode, data);
    }

    private void initFilterButton() {
        button_openFilterDialog = findViewById(R.id.button_openFilterDialog);
        button_openFilterDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new FilterDialogFragment();
                dialog.show(getSupportFragmentManager(), "filterDialog");
            }
        });
    }

    /**
     * Implementing interface method of the FilterDialogFragment listener.
     * */
    @Override
    public void onDialogApplyFilterClick(DialogFragment dialog) {
        mGalleryViewModel.filter();
    }
}
