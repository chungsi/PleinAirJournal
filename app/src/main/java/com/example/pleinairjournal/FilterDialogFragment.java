package com.example.pleinairjournal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class FilterDialogFragment extends DialogFragment {

    private Spinner spinner_location;
    private ChipGroup chipGroup_year, chipGroup_month;
    private GalleryViewModel mGalleryViewModel;
    private String mYear = "", mMonth = "";

    public FilterDialogFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGalleryViewModel = ViewModelProviders.of(getActivity()).get(GalleryViewModel.class);

        initChipGroups(view);
        initFilterSpinners(view);
        initFilterButtons(view);
    }

    private void initFilterButtons(View view) {
        Button button_applyFilters = view.findViewById(R.id.button_applyFilters);
        Button button_resetFilters = view.findViewById(R.id.button_resetFilters);
        Button button_cancelFilter = view.findViewById(R.id.button_cancelFilter);

        button_applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGalleryViewModel.setChipYearIds(chipGroup_year.getCheckedChipIds());
                mGalleryViewModel.setChipMonthIds(chipGroup_month.getCheckedChipIds());
                mGalleryViewModel.setFilterValues(
                        mYear,
                        mMonth,
                        spinner_location.getSelectedItem().toString()
                );

                listener.onDialogApplyFilterClick(FilterDialogFragment.this);
                dismiss();
            }
        });
        button_resetFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_location.setSelection(0, true);
                chipGroup_year.clearCheck();
                chipGroup_month.clearCheck();
            }
        });
        button_cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initFilterSpinners(View view) {
        spinner_location = view.findViewById(R.id.spinner_location);

        // To get all locations, need to call the viewModel's function to get all locations from db.
        List<String> locationsList = mGalleryViewModel.getAllLocations();
        ArrayAdapter<String> locationArrayAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.spinner_item,
                locationsList);
        spinner_location.setAdapter(locationArrayAdapter);

        restoreFilterSpinnerPositions();
    }

    private void restoreFilterSpinnerPositions() {
        spinner_location.setSelection(mGalleryViewModel.getLocationIndex());
    }

    /**
     * Initializes the chip groups and sets change listeners to them. Also restores previously
     * saved chips.
     * */
    private void initChipGroups(View view) {
        chipGroup_year = view.findViewById(R.id.chipGroup_year);
        chipGroup_month = view.findViewById(R.id.chipGroup_month);

        chipGroup_year.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null && chip.isChecked()) {
                    mYear = chip.getText().toString();
                } else {
                    mYear = "";
                }
            }
        });

        chipGroup_month.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null && chip.isChecked()) {
                    mMonth = chip.getTag().toString();
                } else {
                    mMonth = "";
                }
            }
        });

        restoreChipGroupFilters();
    }

    /**
     * Checks if filters were previously checked last time the filer dialog was opened, and if so,
     * will re-check them again.
     * */
    private void restoreChipGroupFilters() {
        if (mGalleryViewModel.getChipYearIds() != null) {
            for (Integer i : mGalleryViewModel.getChipYearIds()) {
                chipGroup_year.check(i);
            }
        }
        if (mGalleryViewModel.getChipMonthIds() != null) {
            for (Integer i : mGalleryViewModel.getChipMonthIds()) {
                chipGroup_month.check(i);
            }
        }
    }

    /**
     * Interface to connect the dialog with the implementing activity.
     * */
    public interface FilterDialogListener {
        void onDialogApplyFilterClick(DialogFragment dialog);
    }

    FilterDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (FilterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement FilterDialogListener");
        }
    }
}
