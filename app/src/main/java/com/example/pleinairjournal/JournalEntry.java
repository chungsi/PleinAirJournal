package com.example.pleinairjournal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalEntry {
    public static final String TABLE_NAME = "entry";
    public static final String _ID = "_id";
    public static final String DATE = "date";
    public static final String TIMESTAMP = "timestamp";
    public static final String LOCATION = "location";
    public static final String LONGLAT = "longlat";
    public static final String COMMENT = "comment";
    public static final String IMAGEFILEPATH = "image";
    public static final String CARDINAL_DIRECTION = "cardinal_direction";
    public static final String CARDINAL_DEGREE = "cardinal_degree";
//    public static final String CARDINAL_STRING = "cardinal_string";

    /**
     * Here starts some test code for instantiating a JournalEntry object.
     * TODO: determine if setter methods are necessary, as we are using the object as an interface
     * */
    private long mId, mTimestamp;
    private String mLocation, mComment, mImageFilePath, mDate, mTime;
    private String mCardinalDirection, mCardinalString;
    private int mCardinalDegree;
    private Date mSqlDate;
    private JournalEntry mEntry;

    // Empty JournalEntry
    public JournalEntry() {}

    // TODO: more fields will be added as needed while developing
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
        mDate = new SimpleDateFormat("EEEE, LLLL d, YYYY").format(timestamp);
        mTime = new SimpleDateFormat("h:mm a").format(timestamp);
        mSqlDate = new Date(timestamp);
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

    public long getId() { return mId; }
    public long getTimestamp() { return mTimestamp; }
    public String getDate() { return mDate; }
    public String getTime() { return mTime; }
    public String getLocation() { return mLocation; }
    public String getComment() { return mComment; }
    public String getCardinalDirection() { return mCardinalDirection; }
    public int getCardinalDegree() { return mCardinalDegree; }
    public String getCardinalString() { return mCardinalDegree + "° " + mCardinalDirection; }

    public String getImageFilePath() { return mImageFilePath; }
    // TODO: cleanup the Bitmap Factory stuff, and find out what's the best way to conserve memory
    public Bitmap getBitmapImage() {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(mImageFilePath, bmpFactoryOptions);
    }
}
