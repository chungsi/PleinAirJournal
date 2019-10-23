package com.example.pleinairjournal;

public class Entry {
    public static final String DATABASE_NAME = "PleinAirJournal.db";
    public static final String TABLE_NAME = "entry";
    public static final String UID = "_id";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String LOCATION = "location";
    public static final String LONGLAT = "longlat";
    public static final String COMMENT = "comment";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_TABLE =
            "CREATE TABLE " +
                    TABLE_NAME + " (" +
                    UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATE + " TEXT, " +
                    LOCATION + " TEXT, " +
                    COMMENT + " TEXT);";
}
