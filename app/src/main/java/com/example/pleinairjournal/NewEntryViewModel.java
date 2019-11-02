package com.example.pleinairjournal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.sql.Timestamp;

public class NewEntryViewModel extends AndroidViewModel {
    public String location, comment, imageFilePath;
    private JournalDb mDb;
    private String mLocation, mComment, mPhotoPath; // maybe it's better to use private vars?
    private long mId, mTimestamp;
//    private JournalEntry mEntry;

    public NewEntryViewModel(@NonNull Application application) {
        super(application);
        mDb = new JournalDb(application);
        mTimestamp = System.currentTimeMillis();
        location = "";
        comment = "";
        imageFilePath = "";
    }

//    public void setEntry() {
//        mEntry = new JournalEntry(location, comment);
//    }

    public long insertEntry() {
        return mDb.insertEntry(mTimestamp, location, comment, imageFilePath);
    }

    public long updateEntry() {
//        return mDb.updateEntry(new JournalEntry((int)mId, location, comment));
        return 0;
    }

    public long getTimestamp() {
        return mTimestamp;
    }
}
