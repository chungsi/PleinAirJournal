package com.example.pleinairjournal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ViewEntryActivity extends ViewEntryMenu
                               implements DeleteConfirmationDialogFragment.DeleteConfirmationDialogListener {
    static final int UPDATE_ENTRY = 2;

    private TextView text_location, text_comment, text_date, text_time, text_cardinal;
    private ViewEntryViewModel mViewModel;
    private ImageView image_photoPreview;

    long mEntryId;
    int mGalleryAdapterPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);
        super.initMenuButtons();

        mViewModel = ViewModelProviders.of(this).get(ViewEntryViewModel.class);

        mEntryId = getIntent().getLongExtra("id", -1);
        mGalleryAdapterPosition = getIntent().getIntExtra("position", -1);

        text_location = findViewById(R.id.text_viewLocation);
        text_comment = findViewById(R.id.text_viewComment);
        text_date = findViewById(R.id.text_date);
        text_time = findViewById(R.id.text_time);
        text_cardinal = findViewById(R.id.text_cardinal);
        image_photoPreview = findViewById(R.id.image_viewPhotoPreview);

        buttonClickListeners();

        // ViewModel to handle updating of information when "Update" action is performed by the user.
        mViewModel.getEntry(mEntryId).observe(this, new Observer<JournalEntry>() {
            @Override
            public void onChanged(JournalEntry entry) {
                text_location.setText(entry.getLocation());
                text_comment.setText(entry.getComment());
                text_date.setText(entry.getDate());
                text_time.setText(entry.getTime());
                text_cardinal.setText(entry.getCardinalString());

                // Here we use the Glide library to efficiently load Bitmap images at the right
                // size to avoid memory overload and slow performance issues.
                Glide.with(getApplicationContext())
                        .load(entry.getImageFilePath())
                        .transition(withCrossFade())
                        .into(image_photoPreview);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == UPDATE_ENTRY) {
            Toast.makeText(this, "Entry updated", Toast.LENGTH_SHORT).show();
            mViewModel.refreshEntry();
            text_location.setText(mViewModel.getLocation());
            text_comment.setText(mViewModel.getComment());
            text_date.setText(mViewModel.getDate());
            text_time.setText(mViewModel.getTime());
            text_cardinal.setText(mViewModel.getCardinalString());

            Glide.with(getApplicationContext())
                    .load(mViewModel.getImageFilePath())
                    .transition(withCrossFade())
                    .into(image_photoPreview);
        }
    }

    private void buttonClickListeners() {
        buttonUpdateEntryClickListener();
        buttonDeleteEntryClickListener();
    }

    private void buttonUpdateEntryClickListener() {
        super.getMenuUpdateButton().setOnClickListener(new View.OnClickListener() {
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
        super.getMenuDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new DeleteConfirmationDialogFragment();
                dialog.show(getSupportFragmentManager(), "deleteConfirmationDialog");
            }
        });
    }

    @Override
    public void onDialogConfirmDeleteClick() {
        mViewModel.deleteEntry(mViewModel.getEntryId());

        Intent replyIntent = new Intent();
        replyIntent.putExtra("id", mViewModel.getEntryId());
        replyIntent.putExtra("position", mGalleryAdapterPosition);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
