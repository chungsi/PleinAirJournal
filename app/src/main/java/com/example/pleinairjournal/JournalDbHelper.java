package com.example.pleinairjournal;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JournalDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "PleinAirJournal.db";
    private Context mContext;

    private static final String CREATE_TABLE =
            "CREATE TABLE " +
                    JournalEntry.TABLE_NAME + " (" +
                    JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    JournalEntry.TIMESTAMP + " TEXT, " +
                    JournalEntry.IMAGEFILEPATH + " TEXT, " +
                    JournalEntry.LOCATION + " TEXT, " +
                    JournalEntry.CARDINAL_DEGREE + " TEXT, " +
                    JournalEntry.CARDINAL_DIRECTION + " TEXT, " +
                    JournalEntry.COMMENT + " TEXT);";

    private static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + JournalEntry.TABLE_NAME;


    public JournalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DELETE_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
