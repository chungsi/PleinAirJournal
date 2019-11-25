package com.example.pleinairjournal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GalleryViewModel extends AndroidViewModel {
    private JournalDb mDb;
    private MutableLiveData<List<JournalEntry>> mAllEntries = new MutableLiveData<>();
    private List<String> mLocationsList;
    private String mYearFilter = "",
            mMonthFilter = "",
            mLocationFilter = "";
    private int mYearIndex = 0,
            mMonthIndex = 0,
            mLocationIndex = 0;
    private List<Integer> chipYearIds, chipMonthIds;

    /**
    * This is a class to handle data and data changes while keeping the data bound to the views.
    * The database queries won't need to be called every time there's an orientation change, and
    * allows data to be changed on the fly and still reflect in the UI.
    * */
    public GalleryViewModel(@NonNull Application application) {
        super(application);
        mDb = new JournalDb(application);
        mAllEntries.setValue(mDb.getAllLiveDataEntries().getValue());
        mLocationsList = mDb.getAllLocations();
    }

    public LiveData<List<JournalEntry>> getAllEntries() {
        return mAllEntries;
    }

    public void deleteEntry(JournalEntry entry) {
        mDb.deleteEntry(entry);
    }

    public void refreshEntries() {
        mAllEntries.setValue(mDb.getAllLiveDataEntries().getValue());
    }

    public void setChipYearIds(List<Integer> ids) { chipYearIds = ids; }
    public List<Integer> getChipYearIds() { return chipYearIds; }

    public void setChipMonthIds(List<Integer> ids) { chipMonthIds = ids; }
    public List<Integer> getChipMonthIds() { return chipMonthIds; }

    public void setFilterValues(String year, String month, String location) {
        mYearFilter = year;
        mMonthFilter = month;
        mLocationFilter = location;
    }

    /**
     * Resets the filter values of the tag passed in from the ViewModel.
     * */
    public void resetFilterValuesByTag(String tag) {
        if (tag.equals("year")) {
            mYearFilter = "";
            setChipYearIds(null);
        }
        else if (tag.equals("month")) {
            mMonthFilter = "";
            setChipMonthIds(null);
        }
        else if (tag.equals("location")) mLocationFilter = "";
    }

    public int getYearIndex() { return mYearIndex; }
    public int getMonthIndex() { return mMonthIndex; }
    public int getLocationIndex() { return mLocationIndex; }

    /**
     * Get all applied filters in a HashMap.
     * */
    public HashMap<String, List<String>> getAppliedFilters() {
        HashMap<String, List<String>> list = new HashMap<>();
        list.put("year", Arrays.asList(mYearFilter));
        list.put("month", Arrays.asList(mMonthFilter));
        list.put("location", Arrays.asList(mLocationFilter));

        return list;
    }

    public void filter() {
        mAllEntries.setValue(mDb.filterBy(mYearFilter, mMonthFilter, mLocationFilter));
    }

    public void filterBy(String year, String month, String location) {
        mAllEntries.setValue(mDb.filterBy(year, month, location));
    }

    public List<String> getAllLocations() {
        return mLocationsList;
    }
}
