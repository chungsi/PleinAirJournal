package com.example.pleinairjournal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class UpdateEntryActivity extends AppCompatActivity {

    private Button button_updateEntry;
    private EditText edit_updateLocation, edit_updateComment;
    private long mEntryId;
    private UpdateEntryViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);

        mEntryId = getIntent().getLongExtra("id", -1);
        Log.i("PLEINAIR_DEBUG", "intent id from UpdateEntry: " + mEntryId);

        button_updateEntry = findViewById(R.id.button_updateEntry);
        edit_updateLocation = findViewById(R.id.edit_updateLocation);
        edit_updateComment = findViewById(R.id.edit_updateComment);

        mViewModel = ViewModelProviders.of(this).get(UpdateEntryViewModel.class);
        mViewModel.getEntry(mEntryId).observe(this, new Observer<JournalEntry>() {
            @Override
            public void onChanged(JournalEntry journalEntry) {
                edit_updateLocation.setText(journalEntry.getLocation());
                edit_updateComment.setText(journalEntry.getComment());
            }
        });

        /*
        * When Update button is clicked, updates entry and sends intent back to the ViewEntryActivity.
        * */
        button_updateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.updateEntry(
                        edit_updateLocation.getText().toString(),
                        edit_updateComment.getText().toString());

                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }
}
