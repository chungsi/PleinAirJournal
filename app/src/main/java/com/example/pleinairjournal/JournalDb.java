package com.example.pleinairjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class JournalDb {
    private SQLiteDatabase mDb;
    private Context mContext;
    private final JournalDbHelper mHelper;

    private List<JournalEntry> mListOfEntries;

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

    /**
     * Inserts a JournalEntry into the DB. Calls the AsyncTask to complete.
     * */
    public void insertEntry(
            long timestamp,
            String location,
            String comment,
            String imageFilePath,
            int cardinalDegree,
            String cardinalDirec
    ) {
        JournalEntry entry = new JournalEntry(timestamp, location, comment, imageFilePath, cardinalDegree, cardinalDirec);
        mDb = mHelper.getWritableDatabase();
        new insertEntryTask(mDb).execute(entry);
    }

    /**
     * AsyncTask to insert entries.
     * */
    public class insertEntryTask extends AsyncTask<JournalEntry, Void, Void> {
        private SQLiteDatabase mDb;

        insertEntryTask(SQLiteDatabase dbHelper) {
            mDb = dbHelper;
        }

        @Override
        protected Void doInBackground(JournalEntry... journalEntries) {
            ContentValues cv = new ContentValues();
            cv.put(JournalEntry.TIMESTAMP, journalEntries[0].getTimestamp());
            cv.put(JournalEntry.LOCATION, journalEntries[0].getLocation());
            cv.put(JournalEntry.COMMENT, journalEntries[0].getComment());
            cv.put(JournalEntry.IMAGEFILEPATH, journalEntries[0].getImageFilePath());
            cv.put(JournalEntry.CARDINAL_DEGREE, journalEntries[0].getCardinalDegree());
            cv.put(JournalEntry.CARDINAL_DIRECTION, journalEntries[0].getCardinalDirection());

            mDb.insert(JournalEntry.TABLE_NAME, null, cv);
            return null;
        }
    }

    /**
     * Updates the entry's location and comment.
     * */
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

    /**
     * Delete an entry by its id.
     * */
    public void deleteEntry(long id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(JournalEntry.TABLE_NAME,
                JournalEntry._ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Delete an entry by passing in a JournalEntry object.
     * */
    public void deleteEntry(JournalEntry entry) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(JournalEntry.TABLE_NAME,
                JournalEntry._ID + " = ?",
                new String[]{String.valueOf(entry.getId())});
    }

    /**
     * Get an entry by its id.
     * */
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

    /**
     * Return a LiveData instance of a JournalEntry by its id.
     * */
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
     * Get all entries and return them in a list.
     * */
    private List<JournalEntry> getAllEntries() {
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
        return getJournalEntriesFromCursor(cursor);
    }

    /** Get a LiveData instance of the list of all JournalEntries.
     * */
    public LiveData<List<JournalEntry>> getAllLiveDataEntries() {
        List<JournalEntry> entries = getAllEntries();

        MutableLiveData<List<JournalEntry>> finalEntries = new MutableLiveData<>();
        finalEntries.setValue(entries);

        return finalEntries;
    }

    /**
     * Gets all distinct locations entered by the user, drawing from the LOCATION column.
     * */
    public List<String> getAllLocations() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String[] column = { JournalEntry.LOCATION };

        Cursor cursor = db.query(
                true,
                JournalEntry.TABLE_NAME,
                column,
                null,
                null,
                JournalEntry.LOCATION,
                null,
                ORDER_CHRONOLOGICAL,
                null
        );

        List<String> locations = new ArrayList<>();
        locations.add(""); // instantiate a default empty value
        while(cursor.moveToNext()) {
            String thisLocation = cursor.getString(cursor.getColumnIndexOrThrow(JournalEntry.LOCATION));
            locations.add(thisLocation);
        }
        cursor.close();

        return locations;
    }

    /**
     * Filter by certain parameters in a "AND" relationship.
     * */
    public List<JournalEntry> filterBy(String year, String month, String location) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ArrayList<String> selectionList = new ArrayList<>();
        String selection = "";

        /*
        * Here is filtering the year and month values passed, to be sure they're not empty.
        * A query is built up from what valid values are passed.
        * */
        if (!year.isEmpty()) {
            selectionList.add(getYearQuery(year));
        }
        if (!month.isEmpty()) {
            month = getMonthNumFromName(month);
            selectionList.add(getMonthQuery(month));
        }
        if (!location.isEmpty()) {
            selectionList.add(getLocationQuery(location));
        }
        for (int i = 0; i < selectionList.size(); i++) {
            if (i == 0) selection += selectionList.get(i);
            else selection += " AND " + selectionList.get(i);
        }

        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                mAllColumns,
                selection,
                null,
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
     * The below date-time query methods are to handle timestamps for SQLite.
     * Currently, timestamps are stored as milliseconds, but SQLite parses seconds, so that is
     * computed. As well, the timestamps are formatted to be in unixepoch format.
     *
     * @param value The value appended at the end of the query. Can be '?' or a literal string.
     * @return The query string for filtering again a year value.
     * */
    private String getYearQuery(String value) {
        return "strftime('%Y', date(" + JournalEntry.TIMESTAMP + "/1000, 'unixepoch')) = '" + value + "'";
    }

    private String getMonthQuery(String value) {
        return "strftime('%m', date(" + JournalEntry.TIMESTAMP + "/1000, 'unixepoch')) = '" + value + "'";
    }

    private String getMonthNumFromName(String name) {
        return String.valueOf(Month.valueOf(name.toUpperCase()).getValue());
    }

    private String getLocationQuery(String loc) {
        return JournalEntry.LOCATION + " = '" + loc + "'";
    }
}
