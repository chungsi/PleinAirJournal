package com.example.pleinairjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class JournalDb {
    private SQLiteDatabase mDb;
    private Context mContext;
    private final JournalDbHelper mHelper;

    private String[] mAllColumns = {
            JournalEntry._ID,
            JournalEntry.TIMESTAMP,
            JournalEntry.IMAGEFILEPATH,
            JournalEntry.LOCATION,
            JournalEntry.COMMENT,
    };

    private static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + JournalEntry.TABLE_NAME;

    private static final String ORDER_CHRONOLOGICAL = JournalEntry.TIMESTAMP + " DESC";

    public JournalDb(Context c) {
        mContext = c;
        mHelper = new JournalDbHelper(mContext);
    }

    public int getEntriesCount() {
        return 0;
    }

    /**
     * Not sure if need this method that inserts with individual strings...
     * */
    public long insertEntry(long timestamp, String location, String comment, String imageFilePath) {
        mDb = mHelper.getWritableDatabase();

        Log.i("PLEINAIR_DEBUG", "inside insertEntry, imagefilepath: " + imageFilePath);

        ContentValues cv = new ContentValues();
        cv.put(JournalEntry.TIMESTAMP, timestamp);
        cv.put(JournalEntry.LOCATION, location);
        cv.put(JournalEntry.COMMENT, comment);
        cv.put(JournalEntry.IMAGEFILEPATH, imageFilePath);
//        cv.put(JournalEntry.DATE, date);

        return mDb.insert(JournalEntry.TABLE_NAME, null, cv);
    }

    /**
     * Or if it's better to just pass in a journal entry object?
     * */
//    public long insertEntry(JournalEntry entry) {
//        mDb = mHelper.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put(JournalEntry.LOCATION, entry.getLocation());
//        cv.put(JournalEntry.COMMENT, entry.getComment());
////        cv.put(JournalEntry.DATE, date);
//
//        return mDb.insert(JournalEntry.TABLE_NAME, null, cv);
//    }

    public long updateEntry(long id, String location, String comment) {
        mDb = mHelper.getWritableDatabase();

        JournalEntry thisEntry = getEntry(id);

        ContentValues cv = new ContentValues();
        cv.put(JournalEntry.LOCATION, location);
        cv.put(JournalEntry.COMMENT, comment);

        return mDb.update(
                JournalEntry.TABLE_NAME,
                cv,
                JournalEntry._ID + " = ?",
                new String[]{String.valueOf(thisEntry.getId())}
        );
    }

    public void deleteEntry(long id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(JournalEntry.TABLE_NAME,
                JournalEntry._ID + " = ?",
                new String[]{String.valueOf(id)});
        Log.i("PLEINAIR_DEBUG", "An entry has been deleted from the db");
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
                cursor.getLong(cursor.getColumnIndexOrThrow(JournalEntry._ID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(JournalEntry.TIMESTAMP)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.COMMENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.IMAGEFILEPATH)));
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
                cursor.getLong(cursor.getColumnIndexOrThrow(JournalEntry.TIMESTAMP)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.COMMENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.IMAGEFILEPATH)));

        cursor.close();

        MutableLiveData<JournalEntry> liveDataEntry = new MutableLiveData<>();
        liveDataEntry.setValue(entry);

        return liveDataEntry;
    }

    public List<JournalEntry> getAllEntries() {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                null,
                null,
                null,
                null,
                ORDER_CHRONOLOGICAL
        );

        /*
        * Loops through query with Cursor object to create a new List of JournalEntries
        * */
        List<JournalEntry> entries = new ArrayList<>();
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(JournalEntry._ID));
            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(JournalEntry.TIMESTAMP));
            String loc = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION));
            String com = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.COMMENT));
            String fp = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.IMAGEFILEPATH));
            entries.add(new JournalEntry(id, timestamp, loc, com, fp));
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

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                null,
                null,
                null,
                null,
                ORDER_CHRONOLOGICAL
        );

        List<JournalEntry> entries = new ArrayList<>();
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(JournalEntry._ID));
            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(JournalEntry.TIMESTAMP));
            String loc = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION));
            String com = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.COMMENT));
            String fp = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.IMAGEFILEPATH));
            entries.add(new JournalEntry(id, timestamp, loc, com, fp));
        }
        cursor.close();

        MutableLiveData<List<JournalEntry>> finalEntries = new MutableLiveData<>();
        finalEntries.setValue(entries);

        return finalEntries;
    }
}
