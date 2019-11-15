package com.example.pleinairjournal;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class GalleryViewModel extends AndroidViewModel {
    private JournalDb mDb;
    private MutableLiveData<List<JournalEntry>> mAllEntries = new MutableLiveData<>();

    /**
    * This is a class to handle data and data changes while keeping the data bound to the views.
    * The database queries won't need to be called every time there's an orientation change, and
    * allows data to be changed on the fly and still reflect in the UI.
    * */
    public GalleryViewModel(@NonNull Application application) {
        super(application);
        mDb = new JournalDb(application);
        mAllEntries.setValue(mDb.getAllLiveDataEntries().getValue());
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

    public void filterBy(String year, String month) {
        mAllEntries.setValue(mDb.filterBy(year, month));
    }

    public List<String> getAllLocations() {
        return mDb.getAllLocations();
    }
}
