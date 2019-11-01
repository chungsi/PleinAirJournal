package com.example.pleinairjournal;

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
    private long mId;
    private long mTimestamp;
    private String mLocation;
    private String mComment;
    private JournalEntry mEntry;

    // Empty JournalEntry
    public JournalEntry() {}

    // TODO: more fields will be added as needed while developing
    public JournalEntry(
            long id,
            long timestamp,
            String location,
            String comment
    ) {
        mId = id;
        mTimestamp = timestamp;
        mLocation = location;
        mComment = comment;
    }

    public JournalEntry(long id, JournalEntry entry) {
        mId = id;
        mEntry = entry;
    }

    public long getId() { return mId; }
    public long getTimestamp() { return mTimestamp; }
    public String getLocation() { return mLocation; }
//    public void setLocation(String location) { mLocation = location; }
    public String getComment() { return mComment; }
//    public void setComment(String comment) { mComment = comment; }
}
