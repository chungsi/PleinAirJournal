package com.example.pleinairjournal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewEntryActivity extends AppCompatActivity {
    private JournalDb mDb;
    private long mEntryId;
    private JournalEntry mEntry;

    private TextView text_id, text_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        mDb = new JournalDb(this);
        mEntryId = getIntent().getLongExtra("id", -1);

        if (mEntryId != -1) mEntry = mDb.getEntry(mEntryId);
        else mEntry = new JournalEntry();

        text_id = findViewById(R.id.text_viewEntryId);
        text_location = findViewById(R.id.text_viewEntryLocation);

        Log.i("PLEINAIR_DEBUG", "id: " + mEntryId + "; " + mEntry.getId());

        text_id.setText(String.valueOf(mEntry.getId()));
        text_location.setText(mEntry.getLocation());
    }
}
