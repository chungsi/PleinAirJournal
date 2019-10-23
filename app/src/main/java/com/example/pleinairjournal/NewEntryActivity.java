package com.example.pleinairjournal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewEntryActivity extends AppCompatActivity {
    EditText edit_name, edit_comment;
    Button btn_createNewEntry;
    private JournalDb mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        edit_name = findViewById(R.id.edit_name);
        edit_comment = findViewById(R.id.edit_comment);
        btn_createNewEntry = findViewById(R.id.btn_createNewEntry);

        mDb = new JournalDb(this);
    }
}
