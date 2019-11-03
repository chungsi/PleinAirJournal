package com.example.pleinairjournal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class JournalEntry {
    public static final String TABLE_NAME = "entry";
    public static final String _ID = "_id";
    public static final String DATE = "date";
    public static final String TIMESTAMP = "timestamp";
    public static final String LOCATION = "location";
    public static final String LONGLAT = "longlat";
    public static final String COMMENT = "comment";
    public static final String IMAGEFILEPATH = "image";

    /**
     * Here starts some test code for instantiating a JournalEntry object.
     * TODO: determine if setter methods are necessary, as we are using the object as an interface
     * */
    private long mId, mTimestamp;
    private String mLocation, mComment, mImageFilePath;
    private JournalEntry mEntry;

    // Empty JournalEntry
    public JournalEntry() {}

    // TODO: more fields will be added as needed while developing
    public JournalEntry(
            long id,
            long timestamp,
            String location,
            String comment,
            String imageFilePath
    ) {
        mId = id;
        mTimestamp = timestamp;
        mLocation = location;
        mComment = comment;
        mImageFilePath = imageFilePath;
    }

    public JournalEntry(long id, JournalEntry entry) {
        mId = id;
        mEntry = entry;
    }

    public long getId() { return mId; }
    public long getTimestamp() { return mTimestamp; }
    public String getImageFilePath() { return mImageFilePath; }
    // TODO: cleanup the Bitmap Factory stuff, and find out what's the best way to conserve memory
    public Bitmap getBitmapImage() {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(mImageFilePath, bmpFactoryOptions);
    }
    public String getLocation() { return mLocation; }
//    public void setLocation(String location) { mLocation = location; }
    public String getComment() { return mComment; }
//    public void setComment(String comment) { mComment = comment; }
}
