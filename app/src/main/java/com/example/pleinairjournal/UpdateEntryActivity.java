package com.example.pleinairjournal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import java.util.List;

public class UpdateEntryActivity extends ViewEntryMenu {

    private TextView text_date, text_time, text_cardinal;
    private ImageView image_photoPreview;
    private EditText edit_updateComment;
    private AutoCompleteTextView autoComplete_location;
    private SharedPreferences sharedPrefs;
    private Button button_updateEntry;
    private long mEntryId;
    private UpdateEntryViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checks which theme the user has selected
        //Theme can only be changed before setContentView
        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        //checks if user has selected dark colour scheme preference, sets theme if yes
        boolean darkModeChecked = sharedPrefs.getBoolean("DARKBUTTONCHECKED", false);
        if(darkModeChecked){
            setTheme(R.style.style_dark);
        }

        //checks if user has selected light colour scheme preference, sets theme if yes
        boolean lightModeChecked = sharedPrefs.getBoolean("LIGHTBUTTONCHECKED", false);
        if(lightModeChecked){
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_update_entry);

        findMenuButtons();

        mEntryId = getIntent().getLongExtra("id", -1);
        mViewModel = ViewModelProviders.of(this).get(UpdateEntryViewModel.class);
        Log.i("PLEINAIR_DEBUG", "intent id from UpdateEntry: " + mEntryId);

        text_date = findViewById(R.id.text_date);
        text_time = findViewById(R.id.text_time);
        text_cardinal = findViewById(R.id.text_cardinal);
        image_photoPreview = findViewById(R.id.image_viewImagePreview);

        edit_updateComment = findViewById(R.id.edit_updateComment);

        mViewModel.getEntry(mEntryId).observe(this, new Observer<JournalEntry>() {
            @Override
            public void onChanged(JournalEntry journalEntry) {
                text_date.setText(journalEntry.getDate());
                text_time.setText(journalEntry.getTime());
                text_cardinal.setText(journalEntry.getCardinalString());
                autoComplete_location.setText(journalEntry.getLocation());
                edit_updateComment.setText(journalEntry.getComment());

                Glide.with(getApplicationContext())
                        .load(journalEntry.getImageFilePath())
                        .into(image_photoPreview);
            }
        });

        initLocationField();
        initUpdateButton();
    }

    private void initUpdateButton() {
        button_updateEntry = findViewById(R.id.button_updateEntry);

        // When Update button is clicked, updates entry and sends intent back to the ViewEntryActivity.
        button_updateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.updateEntry(
                        autoComplete_location.getText().toString(),
                        edit_updateComment.getText().toString());

                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }

    private void initLocationField() {
        autoComplete_location = findViewById(R.id.autoComplete_location);
        List<String> locationsList = mViewModel.getAllLocations();

        ArrayAdapter<String> locationsArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                locationsList);

        autoComplete_location.setAdapter(locationsArrayAdapter);
        autoComplete_location.setThreshold(1); // just put one letter?
    }
}
