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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends JournalMenu
        implements FilterDialogFragment.FilterDialogListener {
    private CompassViewModel mCompassViewModel;
    private SharedPreferences sharedPrefs;
    private GalleryViewModel mGalleryViewModel;
    private GalleryAdapter mAdapter;
    private ChipGroup chipGroup_appliedFilters;

    TextView text_name, text_viewCardinal;
    Button button_openFilterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        super.initMenuButtonsWithActive("dashboard");
        super.initCompassDisplay();
        initCompass();

        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("USERNAME", "");
        text_name = findViewById(R.id.text_name);
        text_name.setText(username + "'s ");

        initGallery();
        initFilterButton();
        initAppliedFilters();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* When an Intent is received, the message is passed to the adapter to handle any updating
        of entries */
        mAdapter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Initializes the filter button to show the filter dialog when clicked.
     * */
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
     * Initializes the gallery view, with the GalleryViewModel that updates the RecyclerView adapter.
     * */
    private void initGallery() {
        RecyclerView recyclerView = findViewById(R.id.recycler_gallery);
        mAdapter = new GalleryAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Working with the ViewModel, and setting a listener on it to observe data changes
        mGalleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        mGalleryViewModel.getAllEntries().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(List<JournalEntry> journalEntries) {
                mAdapter.setEntries(journalEntries);
            }
        });
    }

    /**
     * Initializes the CompassViewModel and related UI views to bind data changes.
     * */
    private void initCompass() {
        text_viewCardinal = super.getCompassTextView();

        // Compass view model to display and update the cardinal direction view
        mCompassViewModel = ViewModelProviders.of(this).get(CompassViewModel.class);
        mCompassViewModel.compassLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                text_viewCardinal.setText(s);
            }
        });
    }

    private void initAppliedFilters() {
        chipGroup_appliedFilters = findViewById(R.id.chipGroup_appliedFilters);
    }

    /**
     * Implementing interface method of the FilterDialogFragment listener.
     * */
    @Override
    public void onDialogApplyFilterClick(DialogFragment dialog) {
        mGalleryViewModel.filter();
        showAppliedFilters();
    }

    /**
     * Creates and adds chips for the applied filters from the filter dialog.
     * Will clear all chips first, and re-add the necessary chips.
     * */
    private void showAppliedFilters() {
        chipGroup_appliedFilters.removeAllViewsInLayout();
        HashMap<String, List<String>> appliedFiltersList = mGalleryViewModel.getAppliedFilters();

        for (Map.Entry<String, List<String>> entry : appliedFiltersList.entrySet()) {
            for (String s : entry.getValue()) {
                if (!s.isEmpty()) {

                    // adds a chip for each entry, and sets a click listener
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.layout_chip_applied_filter, chipGroup_appliedFilters, false);
                    chip.setText(s);
                    chip.setTag(entry.getKey());

                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String filterTag = view.getTag().toString();

                            ((ChipGroup)view.getParent()).removeView(view);
                            mGalleryViewModel.resetFilterValuesByTag(filterTag);
                            mGalleryViewModel.filter();
                        }
                    });

                    chipGroup_appliedFilters.addView(chip);
                }
            }
        }
    }
}
