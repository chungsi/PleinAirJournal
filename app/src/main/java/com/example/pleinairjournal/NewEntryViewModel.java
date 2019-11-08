package com.example.pleinairjournal;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.sql.Timestamp;

public class NewEntryViewModel extends AndroidViewModel {
    public String location, comment, imageFilePath, cardinal;
    public int cardinalDegree;
    private JournalDb mDb;
    private long mId, mTimestamp;
//    private String mLocation, mComment, mPhotoPath; // maybe it's better to use private vars?
//    private JournalEntry mEntry;

    public NewEntryViewModel(@NonNull Application application) {
        super(application);
        Log.i("PLEINAIR_DEBUG", "NewEntryViewModel created");

        mDb = new JournalDb(application);
        mTimestamp = System.currentTimeMillis();
        location = "";
        comment = "";
        imageFilePath = "";
        cardinal = "";
        cardinalDegree = 0;
    }

//    public void setEntry() {
//        mEntry = new JournalEntry(location, comment);
//    }

    public long insertEntry() {
        Log.i("PLEINAIR_DEBUG", "insertEntry in NewEntryViewModel");
        return mDb.insertEntry(mTimestamp, location, comment, imageFilePath, cardinalDegree, cardinal);
    }

    public long updateEntry() {
//        return mDb.updateEntry(new JournalEntry((int)mId, location, comment));
        return 0;
    }

    public long getTimestamp() {
        return mTimestamp;
    }
}
