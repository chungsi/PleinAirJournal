package com.example.pleinairjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JournalDb {
    private SQLiteDatabase mDb;
    private Context mContext;
    private final JournalDbHelper mHelper;

    private String[] mAllColumns = {
            JournalEntry._ID,
            JournalEntry.DATE,
            JournalEntry.TIMESTAMP,
            JournalEntry.IMAGEFILEPATH,
            JournalEntry.LOCATION,
            JournalEntry.COMMENT,
            JournalEntry.CARDINAL_DEGREE,
            JournalEntry.CARDINAL_DIRECTION
    };

    private static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + JournalEntry.TABLE_NAME;

    private static final String ORDER_CHRONOLOGICAL = JournalEntry.TIMESTAMP + " DESC";

    public JournalDb(Context c) {
        mContext = c;
        mHelper = new JournalDbHelper(mContext);
    }

    /**
     * TODO: fill out this function to return number of db entries.
     * */
    public int getEntriesCount() {
        return 0;
    }

    public long insertEntry(
            long timestamp,
            String location,
            String comment,
            String imageFilePath,
            int cardinalDegree,
            String cardinalDirec
    ) {
        mDb = mHelper.getWritableDatabase();

//        Log.i("PLEINAIR_DEBUG", "inside insertEntry, imagefilepath: " + imageFilePath);

        ContentValues cv = new ContentValues();
        cv.put(JournalEntry.TIMESTAMP, timestamp);
//        cv.put(JournalEntry.DATE, );
        cv.put(JournalEntry.LOCATION, location);
        cv.put(JournalEntry.COMMENT, comment);
        cv.put(JournalEntry.IMAGEFILEPATH, imageFilePath);
        cv.put(JournalEntry.CARDINAL_DEGREE, cardinalDegree);
        cv.put(JournalEntry.CARDINAL_DIRECTION, cardinalDirec);

        return mDb.insert(JournalEntry.TABLE_NAME, null, cv);
    }

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
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.IMAGEFILEPATH)),
                cursor.getInt(cursor.getColumnIndexOrThrow(JournalEntry.CARDINAL_DEGREE)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.CARDINAL_DIRECTION)));
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
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.IMAGEFILEPATH)),
                cursor.getInt(cursor.getColumnIndexOrThrow(JournalEntry.CARDINAL_DEGREE)),
                cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.CARDINAL_DIRECTION)));

        cursor.close();

        MutableLiveData<JournalEntry> liveDataEntry = new MutableLiveData<>();
        liveDataEntry.setValue(entry);

        return liveDataEntry;
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

        List<JournalEntry> entries = getJournalEntriesFromCursor(cursor);

        MutableLiveData<List<JournalEntry>> finalEntries = new MutableLiveData<>();
        finalEntries.setValue(entries);

        return finalEntries;
    }

    /**
     * Filter by certain parameters in a "AND" relationship.
     * */
    public List<JournalEntry> filterBy(String year, String month) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ArrayList<String> selectionList = new ArrayList<>();
        String selection = "";

        /*
        * Here is filtering the year and month values passed, to be sure they're not empty.
        * A query is built up from what valid values are passed.
        * */
        if (!year.isEmpty()) {
            selectionList.add(getYearQuery("'" + year + "'"));
        }
        if (!month.isEmpty()) {
            month = getMonthNumFromName(month);
            selectionList.add(getMonthQuery("'" + month + "'"));
        }
        for (int i = 0; i < selectionList.size(); i++) {
            if (i == 0) selection += selectionList.get(i);
            else selection += " AND " + selectionList.get(i);
        }

        Log.i("PLEINAIR_DEBUG", "query: " + selection + "; value: " + year);

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                selection,
                null,
                null,
                null,
                ORDER_CHRONOLOGICAL
        );

        Log.i("PLEINAIR_DEBUG", "how many returned: " + cursor.getCount());

        return getJournalEntriesFromCursor(cursor);
    }

    public List<JournalEntry> filterByYear(String year) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selection = "strftime('%Y', '" + JournalEntry.TIMESTAMP + "') = ?";
        String[] selectionArgs = { year };

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                selection,
                selectionArgs,
                null,
                null,
                ORDER_CHRONOLOGICAL
        );

        return getJournalEntriesFromCursor(cursor);
    }

    public List<JournalEntry> filterByCardinalDirection(String cardinal) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selection = JournalEntry.CARDINAL_DIRECTION + " = ?";
        String[] selectionArgs = { cardinal };

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                selection,
                selectionArgs,
                null,
                null,
                ORDER_CHRONOLOGICAL
        );

        return getJournalEntriesFromCursor(cursor);
    }

    /**
     * Loops through cursor to create a list of JournalEntries.
     * A method mostly to reduce redundancy in other db calling methods.
     * */
    private List<JournalEntry> getJournalEntriesFromCursor(Cursor cursor) {
        List<JournalEntry> entries = new ArrayList<>();
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(JournalEntry._ID));
            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(JournalEntry.TIMESTAMP));
            String loc = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION));
            String com = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.COMMENT));
            String fp = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.IMAGEFILEPATH));
            int degree = cursor.getInt(cursor.getColumnIndexOrThrow(JournalEntry.CARDINAL_DEGREE));
            String cardinalDirec = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.CARDINAL_DIRECTION));
            entries.add(new JournalEntry(id, timestamp, loc, com, fp, degree, cardinalDirec));
        }
        cursor.close();

        return entries;
    }

    /**
     * @param value The value appended at the end of the query. Can be '?' or a literal string.
     * @return The query string for filtering again a year value.
     *
     * The below date-time query methods are to handle timestamps for SQLite.
     * Currently, timestamps are stored as milliseconds, but SQLite parses seconds, so that is
     * computed. As well, the timestamps are formatted to be in unixepoch format.
     * */
    private String getYearQuery(String value) {
        return "strftime('%Y', date(" + JournalEntry.TIMESTAMP + "/1000, 'unixepoch')) = " + value;
    }

    private String getMonthQuery(String value) {
        return "strftime('%m', date(" + JournalEntry.TIMESTAMP + "/1000, 'unixepoch')) = " + value;
    }

    private String getMonthNumFromName(String name) {
        return String.valueOf(Month.valueOf(name.toUpperCase()).getValue());
    }

}
