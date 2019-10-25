package com.example.pleinairjournal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ViewEntryActivity extends AppCompatActivity {
//    private JournalDb mDb;
//    private long mEntryId;

    private TextView text_id, text_location;
    private Button button_deleteEntry, button_updateEntry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        final long mEntryId = getIntent().getLongExtra("id", -1);

        text_id = findViewById(R.id.text_viewEntryId);
        text_location = findViewById(R.id.text_viewEntryLocation);
        button_deleteEntry = findViewById(R.id.button_deleteEntry);
        button_updateEntry = findViewById(R.id.button_updateEntry);

        /*
        * There's a ViewModel is use here for now, so that when we implement "edit" and "delete"
        * buttons, the changes will automatically show up in this activity.
        * */
        final EntryViewModel mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);
        mEntryViewModel.getEntry(mEntryId).observe(this, new Observer<JournalEntry>() {
            @Override
            public void onChanged(JournalEntry entry) {
                text_id.setText(String.valueOf(entry.getId()));
                text_location.setText(entry.getLocation());
            }
        });

        /*
        * Code to delete works, but need to implement a new Intent so that the recycler view will
        * properly refresh and update itself.
        * */
//        button_deleteEntry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JournalEntry entry = mEntryViewModel.getEntry(mEntryId).getValue();
//                mEntryViewModel.deleteEntry(entry);
//
//                finish();
//            }
//        });

//        button_updateEntry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
}
