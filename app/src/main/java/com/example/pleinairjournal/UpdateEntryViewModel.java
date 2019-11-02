package com.example.pleinairjournal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UpdateEntryViewModel extends AndroidViewModel {
    private JournalDb mDb;
    private LiveData<JournalEntry> mEntry;
    private long mId;

    public UpdateEntryViewModel(@NonNull Application application) {
        super(application);
        mDb = new JournalDb(application);
    }

    public LiveData<JournalEntry> getEntry(long id) {
        mId = id;
        mEntry = mDb.getLiveDataEntry(id);

        return mEntry;
    }

    public long updateEntry(String location, String comment) {
//        JournalEntry entry = new JournalEntry(/mId, location, comment);
        return mDb.updateEntry(mId, location, comment);
    }
}