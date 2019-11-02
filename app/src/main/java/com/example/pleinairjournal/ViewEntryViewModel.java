package com.example.pleinairjournal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewEntryViewModel extends AndroidViewModel {
    private JournalDb mDb;
    private long mId;
    private LiveData<JournalEntry> mEntry;

    public ViewEntryViewModel(@NonNull Application application) {
        super(application);
        mDb = new JournalDb(application);
    }

    public LiveData<JournalEntry> getEntry() {
        if (mEntry == null) {
            mEntry = new MutableLiveData<>();
        }
        return mEntry;
    }

    public LiveData<JournalEntry> getEntry(long id) {
        mId = id;
        mEntry = mDb.getLiveDataEntry(id);
        return mEntry;
    }

    public long getEntryId() {
        return mEntry.getValue().getId();
    }

    public String getLocation() {
        return mEntry.getValue().getLocation();
    }

    public String getComment() {
        return mEntry.getValue().getComment();
    }

    public String getImageFilePath() { return mEntry.getValue().getImageFilePath(); }

    public void refreshEntry() {
        mEntry = mDb.getLiveDataEntry(mId);
    }

    public void deleteEntry(long id) {
        mDb.deleteEntry(id);
    }
}
