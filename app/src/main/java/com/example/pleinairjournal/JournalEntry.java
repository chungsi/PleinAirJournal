package com.example.pleinairjournal;

import java.text.SimpleDateFormat;

public class JournalEntry {
    /**
     * The Database table schema column names are defined here.
     * */
    public static final String TABLE_NAME = "entry";
    public static final String _ID = "_id";
    public static final String DATE = "date";
    public static final String TIMESTAMP = "timestamp";
    public static final String LOCATION = "location";
    public static final String COMMENT = "comment";
    public static final String IMAGEFILEPATH = "image";
    public static final String CARDINAL_DIRECTION = "cardinal_direction";
    public static final String CARDINAL_DEGREE = "cardinal_degree";

    /**
     * The JournalEntry object
     * */
    private long mId, mTimestamp;
    private String mLocation, mComment, mImageFilePath, mDate, mFullDate, mTime, mShortDate;
    private String mCardinalDirection;
    private int mCardinalDegree;
    private JournalEntry mEntry;

    // Empty JournalEntry
    public JournalEntry() {}

    /**
     * Create a JournalEntry without an id.
     * */
    public JournalEntry(
            long timestamp,
            String location,
            String comment,
            String imageFilePath,
            int cardinalDegree,
            String cardinalDirection) {
        mId = -1;
        mTimestamp = timestamp;
        mFullDate = new SimpleDateFormat("EEEE, LLLL d, YYYY").format(timestamp);
        mDate = new SimpleDateFormat("LLLL d, YYYY").format(timestamp);
        mShortDate = new SimpleDateFormat("LLLL d").format(timestamp);
        mTime = new SimpleDateFormat("h:mm a").format(timestamp);
        mLocation = location;
        mComment = comment;
        mImageFilePath = imageFilePath;
        mCardinalDegree = cardinalDegree;
        mCardinalDirection = cardinalDirection;
    }

    /**
     * Create a full JournalEntry with the corresponding database id.
     * */
    public JournalEntry(
            long id,
            long timestamp,
            String location,
            String comment,
            String imageFilePath,
            int cardinalDegree,
            String cardinalDirection
    ) {
        mId = id;
        mTimestamp = timestamp;
        mFullDate = new SimpleDateFormat("EEEE, LLLL d, YYYY").format(timestamp);
        mDate = new SimpleDateFormat("LLLL d, YYYY").format(timestamp);
        mShortDate = new SimpleDateFormat("LLLL d").format(timestamp);
        mTime = new SimpleDateFormat("h:mm a").format(timestamp);
        mLocation = location;
        mComment = comment;
        mImageFilePath = imageFilePath;
        mCardinalDegree = cardinalDegree;
        mCardinalDirection = cardinalDirection;
    }

    public JournalEntry(long id, JournalEntry entry) {
        mId = id;
        mEntry = entry;
    }

    /**
     * Getter methods for a JournalEntry object.
     * */
    public long getId() { return mId; }
    public long getTimestamp() { return mTimestamp; }
    public String getFullDate() { return mFullDate; }
    public String getShortDate() { return mShortDate; }
    public String getDate() { return mDate; }
    public String getDisplayDate() { return mShortDate + ", " + mTime; }
    public String getTime() { return mTime; }
    public String getLocation() { return mLocation; }
    public String getComment() { return mComment; }
    public String getCardinalDirection() { return mCardinalDirection; }
    public int getCardinalDegree() { return mCardinalDegree; }
    public String getCardinalString() { return mCardinalDegree + "° " + mCardinalDirection; }
    public String getImageFilePath() { return mImageFilePath; }
}
