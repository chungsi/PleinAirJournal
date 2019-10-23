package com.example.pleinairjournal;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JournalDbHelper extends SQLiteOpenHelper {

    private Context mContext;

    public JournalDbHelper(Context context) {
        super(context, Entry.DATABASE_NAME, null, Entry.DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Entry.CREATE_TABLE);
        } catch (SQLException e) {
            // catch the exception
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//        try {
//            db.execSQL(DROP_TABLE);
//        }
    }
}
