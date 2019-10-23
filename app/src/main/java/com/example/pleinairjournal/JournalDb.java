package com.example.pleinairjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class JournalDb {
    private SQLiteDatabase mDb;
    private Context mContext;
    private final JournalDbHelper mHelper;

    public JournalDb(Context c) {
        mContext = c;
        mHelper = new JournalDbHelper(mContext);
    }

    public long insertData(String date, String location, String comment) {
        mDb = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Entry.DATE, date);
        cv.put(Entry.LOCATION, location);
        cv.put(Entry.COMMENT, comment);

        return mDb.insert(Entry.TABLE_NAME, null, cv);
    }

    public Cursor getData() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String[] columns = {Entry.UID, Entry.DATE, Entry.LOCATION, Entry.COMMENT};

        return db.query(
                Entry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
    }
}
