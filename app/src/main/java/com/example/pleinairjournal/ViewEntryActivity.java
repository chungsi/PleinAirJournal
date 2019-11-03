package com.example.pleinairjournal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ViewEntryActivity extends AppCompatActivity {
//    private JournalDb mDb;
//    private long mEntryId;

    static final int UPDATE_ENTRY = 2;

    private TextView text_id, text_location, text_comment, text_filePath, text_date, text_time;
    private Button button_deleteEntry, button_updateEntry;
    private ViewEntryViewModel mViewModel;
    private ImageView image_photoPreview;

    long mEntryId;
    int mGalleryAdapterPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        mEntryId = getIntent().getLongExtra("id", -1);
        mGalleryAdapterPosition = getIntent().getIntExtra("position", -1);

        text_id = findViewById(R.id.text_viewId);
        text_location = findViewById(R.id.text_viewLocation);
        text_comment = findViewById(R.id.text_viewComment);
        text_filePath = findViewById(R.id.text_filePath);
        text_date = findViewById(R.id.text_date);
        text_time = findViewById(R.id.text_time);
        button_deleteEntry = findViewById(R.id.button_deleteEntry);
        button_updateEntry = findViewById(R.id.button_updateEntry);
        image_photoPreview = findViewById(R.id.image_photoPreview);

        buttonClickListeners();

        /*
        * There's a ViewModel is use here for now, so that when we implement "edit" and "delete"
        * buttons, the changes will automatically show up in this activity.
        * */
        mViewModel = ViewModelProviders.of(this).get(ViewEntryViewModel.class);
        mViewModel.getEntry(mEntryId).observe(this, new Observer<JournalEntry>() {
            @Override
            public void onChanged(JournalEntry entry) {
                text_id.setText(String.valueOf(entry.getId()));
                text_location.setText(entry.getLocation());
                text_comment.setText(entry.getComment());
                text_filePath.setText(entry.getImageFilePath());
                text_date.setText(entry.getDate());
                text_time.setText(entry.getTime());
                image_photoPreview.setImageBitmap(entry.getBitmapImage());
                Log.i("PLEINAIR_DEBUG", "timestamp: " + entry.getTimestamp() +
                        "; date: " + entry.getDate());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UPDATE_ENTRY) {
            Toast.makeText(this, "Entry updated", Toast.LENGTH_SHORT).show();
            mViewModel.refreshEntry();
            text_id.setText(String.valueOf(mViewModel.getEntryId()));
            text_location.setText(mViewModel.getLocation());
            text_comment.setText(mViewModel.getComment());
            text_filePath.setText(mViewModel.getImageFilePath());
            text_date.setText(mViewModel.getDate());
            text_time.setText(mViewModel.getTime());
            image_photoPreview.setImageBitmap(mViewModel.getBitmapImage());
        }
    }

    private void buttonClickListeners() {
        buttonUpdateEntryClickListener();
        buttonDeleteEntryClickListener();
    }

    private void buttonUpdateEntryClickListener() {
        button_updateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UpdateEntryActivity.class);
                i.putExtra("id", mViewModel.getEntryId());
                Log.i("PLEINAIR_DEBUG", "intent id from ViewEntry: " + mViewModel.getEntryId());
                startActivityForResult(i, UPDATE_ENTRY);
            }
        });
    }

    private void buttonDeleteEntryClickListener() {
        button_deleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.deleteEntry(mViewModel.getEntryId());

                Intent replyIntent = new Intent();
                replyIntent.putExtra("id", mViewModel.getEntryId());
                replyIntent.putExtra("position", mGalleryAdapterPosition);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }
}
