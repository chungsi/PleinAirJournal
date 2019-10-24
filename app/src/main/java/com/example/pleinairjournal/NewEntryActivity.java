package com.example.pleinairjournal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewEntryActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edit_location, edit_comment;
    Button button_createNewEntry;
    private JournalDb mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        edit_location = findViewById(R.id.edit_location);
        edit_comment = findViewById(R.id.edit_comment);
        button_createNewEntry = findViewById(R.id.button_createNewEntry);
        button_createNewEntry.setOnClickListener(this);

        mDb = new JournalDb(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(button_createNewEntry)) {
            // send data to a data filtering/cleaning function
            // call db to insert data
            long id = mDb.insertEntry(edit_location.getText().toString(), edit_comment.getText().toString());

            if (id != -1) {
                Toast.makeText(this, "Added entry!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
