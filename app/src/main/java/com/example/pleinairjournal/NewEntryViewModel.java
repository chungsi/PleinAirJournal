package com.example.pleinairjournal;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class NewEntryViewModel extends AndroidViewModel {
    private String mLocation, mComment, mImageFilePath, mCardinal;
    public int mCardinalDegree;
    private JournalDb mDb;
    private long mId, mTimestamp;

    public NewEntryViewModel(@NonNull Application application) {
        super(application);
        Log.i("PLEINAIR_DEBUG", "NewEntryViewModel created");

        mDb = new JournalDb(application);
        mTimestamp = System.currentTimeMillis();
        mLocation = "";
        mComment = "";
        mImageFilePath = "";
        mCardinal = "";
        mCardinalDegree = 0;
    }

    public void setLocation(String loc) { mLocation = loc; }
    public void setComment(String com) { mComment = com; }
    public void setCardinalDegree(int deg) { mCardinalDegree = deg; }
    public void setCardinal(String cardinal) { mCardinal = cardinal; }
    public void setImageFilePath(String fp) { mImageFilePath = fp; }
//    public long getTimestamp() {
//        return mTimestamp;
//    }
    public String getImageFilePath() { return mImageFilePath; }
    public List<String> getAllLocations() { return mDb.getAllLocations(); }

    /**
     * Creates a filename for a new camera photo, and returns a File object for use.
     * */
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = new File(getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),
                "PleinAirJournal");
        if (!storageDir.exists()) storageDir.mkdir();

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        setImageFilePath(image.getAbsolutePath());

        return image;
    }

    public void insertEntry() {
        Log.i("PLEINAIR_DEBUG", "insertEntry in NewEntryViewModel");
        mDb.insertEntry(mTimestamp, mLocation, mComment, mImageFilePath, mCardinalDegree, mCardinal);
    }
}
