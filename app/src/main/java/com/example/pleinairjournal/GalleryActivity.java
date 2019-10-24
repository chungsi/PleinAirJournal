package com.example.pleinairjournal;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private JournalDb mDb;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mDb = new JournalDb(this);

        tempShowAllEntries();

        mRecyclerView = findViewById(R.id.recycler_gallery);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // TODO: make db access in an AsyncTask task, and maybe show loading sign in the meanwhile
        mAdapter = new GalleryAdapter(this, mDb.getAllEntries());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        // release the db helper, but need to figure this out
//        mDbHelper.close();
        super.onDestroy();
    }

    /**
     * Temp function to just list all the entries for testing.
     *
     * */
    private void tempShowAllEntries() {
        List<JournalEntry> entries = mDb.getAllEntries();

        TextView dump = findViewById(R.id.text_dump);
        for (JournalEntry entry : entries) {
            dump.append(entry.getId() + ": " + entry.getLocation() + ", " + entry.getComment() + "\n");
        }
    }
}
