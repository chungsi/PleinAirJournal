package com.example.pleinairjournal;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JournalDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PleinAirJournal.db";
    private Context mContext;

    private static final String CREATE_TABLE =
            "CREATE TABLE " +
                    JournalEntry.TABLE_NAME + " (" +
                    JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    JournalEntry.DATE + " TEXT, " +
                    JournalEntry.LOCATION + " TEXT, " +
                    JournalEntry.COMMENT + " TEXT);";

    private static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + JournalEntry.TABLE_NAME;


    public JournalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, JournalEntry.DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
            // catch the exception
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL(DELETE_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            // catch exception
        }
    }
}
