package com.example.pleinairjournal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class EntryViewModel extends AndroidViewModel {
    private JournalDb mDb;
    private LiveData<JournalEntry> mEntry;

    public EntryViewModel(@NonNull Application application) {
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
        mEntry = mDb.getLiveDataEntry(id);
        return mEntry;
    }

    public void deleteEntry(JournalEntry entry) {
        mDb.deleteEntry(entry);
    }
}
