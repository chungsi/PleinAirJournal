package com.example.pleinairjournal;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class JournalRepository {
    private JournalDb mDb;
    private LiveData<List<JournalEntry>> mAllEntries;

    /**
     * This is another layer of abstraction on top of the JournalDb.
     * When properly implemented, we should cache data that is queried so queries aren't called
     * again and again, thus wasting resources and time.
     *
     * Data should be stored in a cache, and called again.
     * */
    JournalRepository(Application application) {

    }
}
