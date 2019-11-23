package com.example.pleinairjournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

    private Spinner spinner_year, spinner_month, spinner_location;
    private ChipGroup chipGroup_year, chipGroup_month;
    private GalleryViewModel mGalleryViewModel;
    private int mYearIndex, mMonthIndex, mLocationIndex;
    private String mYear = "", mMonth = "";
//    private View mDialog;

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
//                mGalleryViewModel.setFilterValues(
//                        spinner_year.getSelectedItem().toString(),
//                        spinner_year.getSelectedItemPosition(),
//                        spinner_month.getSelectedItem().toString(),
//                        spinner_month.getSelectedItemPosition(),
//                        spinner_location.getSelectedItem().toString(),
//                        spinner_location.getSelectedItemPosition()
//                );

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
                spinner_year.setSelection(0, true);
                spinner_month.setSelection(0, true);
                spinner_location.setSelection(0, true);
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
        spinner_year = view.findViewById(R.id.spinner_year);
        spinner_month = view.findViewById(R.id.spinner_month);
        spinner_location = view.findViewById(R.id.spinner_location);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.year_array,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(arrayAdapter);

        arrayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.month_array,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(arrayAdapter);

        // To get all locations, need to call the viewModel's function to get all locations from db.
        List<String> locationsList = mGalleryViewModel.getAllLocations();
        ArrayAdapter<String> locationArrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                locationsList);
        spinner_location.setAdapter(locationArrayAdapter);

        restoreFilterSpinnerPositions();
    }

    private void restoreFilterSpinnerPositions() {
        spinner_year.setSelection(mGalleryViewModel.getYearIndex());
        spinner_month.setSelection(mGalleryViewModel.getMonthIndex());
        spinner_location.setSelection(mGalleryViewModel.getLocationIndex());
    }

    private void initChipGroups(View view) {
        chipGroup_year = view.findViewById(R.id.chipGroup_year);
        chipGroup_month = view.findViewById(R.id.chipGroup_month);

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
                    mMonth = chip.getText().toString();
                } else {
                    mMonth = "";
                }
            }
        });
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
