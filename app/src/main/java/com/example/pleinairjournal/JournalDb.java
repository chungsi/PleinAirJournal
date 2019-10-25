package com.example.pleinairjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class JournalDb {
    private SQLiteDatabase mDb;
    private Context mContext;
    private final JournalDbHelper mHelper;

    private String[] mAllColumns = {JournalEntry._ID, JournalEntry.DATE, JournalEntry.LOCATION, JournalEntry.COMMENT};


    public JournalDb(Context c) {
        mContext = c;
        mHelper = new JournalDbHelper(mContext);
    }

    public int getEntriesCount() {
        return 0;
    }

    public long insertEntry(String location, String comment) {
        mDb = mHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(JournalEntry.LOCATION, location);
        cv.put(JournalEntry.COMMENT, comment);
//        cv.put(JournalEntry.DATE, date);

        return mDb.insert(JournalEntry.TABLE_NAME, null, cv);
    }

    public long updateEntry(JournalEntry entry) {
        mDb = mHelper.getWritableDatabase();

        JournalEntry thisEntry = getEntry(entry.getId());

        ContentValues cv = new ContentValues();
        cv.put(JournalEntry.LOCATION, entry.getLocation());
        cv.put(JournalEntry.COMMENT, entry.getComment());

        return mDb.update(
                JournalEntry.TABLE_NAME,
                cv,
                JournalEntry._ID + " = ?",
                new String[]{String.valueOf(thisEntry.getId())}
        );
    }

    public void deleteEntry(JournalEntry entry) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(JournalEntry.TABLE_NAME,
                JournalEntry._ID + " = ?",
                new String[]{String.valueOf(entry.getId())});
        Log.i("PLEINAIR_DEBUG", "An entry has been deleted from the db");
    }

    public JournalEntry getEntry(long id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String[] thisId = {String.valueOf(id)};

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                JournalEntry._ID + "=?",
                thisId,
                null,
                null,
                null
        );

        if (cursor == null)
            return new JournalEntry();

        cursor.moveToFirst();
        JournalEntry entry = new JournalEntry(
                cursor.getInt(cursor.getColumnIndexOrThrow(JournalEntry._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.COMMENT)));
        cursor.close();

        return entry;
    }

    public LiveData<JournalEntry> getLiveDataEntry(long id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String[] thisId = {String.valueOf(id)};

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                JournalEntry._ID + "=?",
                thisId,
                null,
                null,
                null
        );

        if (cursor == null)
            return new MutableLiveData<>();

        cursor.moveToFirst();
        JournalEntry entry = new JournalEntry(
                cursor.getInt(cursor.getColumnIndexOrThrow(JournalEntry._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.COMMENT)));
        cursor.close();

        MutableLiveData<JournalEntry> liveDataEntry = new MutableLiveData<>();
        liveDataEntry.setValue(entry);

        return liveDataEntry;
    }

    public List<JournalEntry> getAllEntries() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
//        String[] columns = {JournalEntry._ID, JournalEntry.DATE, JournalEntry.LOCATION, JournalEntry.COMMENT};

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                null,
                null,
                null,
                null,
                null
        );

        /*
        * Loops through query with Cursor object to create a new List of JournalEntries
        * */
        List<JournalEntry> entries = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(JournalEntry._ID));
            String loc = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION));
            String com = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.COMMENT));
            entries.add(new JournalEntry(id, loc, com));
        }
        cursor.close();

        return entries;
    }

    /**
     * TODO: make db access in an AsyncTask task, and maybe show loading sign in the meanwhile
     * Test method to use LiveData objects with a ViewModel
     * */
    public LiveData<List<JournalEntry>> getAllLiveDataEntries() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
//        String[] columns = {JournalEntry._ID, JournalEntry.DATE, JournalEntry.LOCATION, JournalEntry.COMMENT};

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                null,
                null,
                null,
                null,
                null
        );

        List<JournalEntry> entries = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(JournalEntry._ID));
            String loc = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION));
            String com = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.COMMENT));
            entries.add(new JournalEntry(id, loc, com));
        }
        cursor.close();

        MutableLiveData<List<JournalEntry>> finalEntries = new MutableLiveData<>();
        finalEntries.setValue(entries);

        return finalEntries;
    }
}
