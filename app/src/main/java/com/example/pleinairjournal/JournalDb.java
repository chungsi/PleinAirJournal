package com.example.pleinairjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public long insertEntry(String location, String comment) {
        mDb = mHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(JournalEntry.LOCATION, location);
        cv.put(JournalEntry.COMMENT, comment);
//        cv.put(JournalEntry.DATE, date);

        return mDb.insert(JournalEntry.TABLE_NAME, null, cv);
    }

    public int updateEntry() {
        //
        return 0;
    }

    public void deleteEntry() {
        //
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

    public int getEntriesCount() {
        return 0;
    }
}
