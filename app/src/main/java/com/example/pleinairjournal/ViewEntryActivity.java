package com.example.pleinairjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ViewEntryActivity extends ViewEntryMenu
                               implements DeleteConfirmationDialogFragment.DeleteConfirmationDialogListener {
    static final int UPDATE_ENTRY = 2;

    private TextView text_location, text_comment, text_date, text_cardinal;
    private ViewEntryViewModel mViewModel;
    private ImageView image_photoPreview;

    long mEntryId;
    int mGalleryAdapterPosition;
    private boolean hasEntryBeenUpdated = false;

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
        text_cardinal = findViewById(R.id.text_cardinal);
        image_photoPreview = findViewById(R.id.image_viewPhotoPreview);

        buttonClickListeners();

        // ViewModel to handle updating of information when "Update" action is performed by the user.
        mViewModel.getEntry(mEntryId).observe(this, new Observer<JournalEntry>() {
            @Override
            public void onChanged(JournalEntry entry) {
                text_location.setText(entry.getLocation());
                text_comment.setText(entry.getComment());
                text_date.setText(entry.getDisplayDate());
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

    /**
     * Listens for the event that the entry has been updated successfully, and then updates all
     * views with the new information.
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the update entry has set the flag that it should be deleted
        if (resultCode == RESULT_OK &&
                requestCode == UPDATE_ENTRY &&
                data.hasExtra("isDeleteEntry")) {
            deleteEntryAndGoToDashboard();
        }
        else if (resultCode == RESULT_OK && requestCode == UPDATE_ENTRY) {
//            Toast.makeText(this, "Entry updated", Toast.LENGTH_SHORT).show();
            mViewModel.refreshEntry();
            text_location.setText(mViewModel.getLocation());
            text_comment.setText(mViewModel.getComment());
            text_date.setText(mViewModel.getDisplayDate());
            text_cardinal.setText(mViewModel.getCardinalString());

            hasEntryBeenUpdated = true; // set flag for back button behaviour

            Glide.with(getApplicationContext())
                    .load(mViewModel.getImageFilePath())
                    .transition(withCrossFade())
                    .into(image_photoPreview);
        }
    }

    private void buttonClickListeners() {
        buttonUpdateEntryClickListener();
        buttonDeleteEntryClickListener();
        buttonBackClickListener();
    }

    /**
     * Will send the user to the UpdateEntryActivity
     * */
    private void buttonUpdateEntryClickListener() {
        super.getMenuUpdateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UpdateEntryActivity.class);
                i.putExtra("id", mViewModel.getEntryId());
                startActivityForResult(i, UPDATE_ENTRY);
            }
        });
    }

    /**
     * Opens the delete confirmation dialog
     * */
    private void buttonDeleteEntryClickListener() {
        super.getMenuDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new DeleteConfirmationDialogFragment();
                dialog.show(getSupportFragmentManager(), "deleteConfirmationDialog");
            }
        });
    }

    /**
     * If the entry has been updated, will send the user back to the dashboard as a new intent,
     * in order to force the data to be updated.
     * Otherwise, just go back using cached data.
     * */
    private void buttonBackClickListener() {
        super.getMenuBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasEntryBeenUpdated) {
                    Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(i);
                } else {
                    finish();
                }
            }
        });
    }

    /**
     * Implements interface for the deletion dialog. Will delete the view and send the user back to
     * the dashboard.
     * */
    @Override
    public void onDialogConfirmDeleteClick() {
        deleteEntryAndGoToDashboard();
    }

    /**
     * Deletes the current entry and takes the user back to the previous activity in the stack.
     * It's a separate function because this action can be taken in two cases:
     * 1. when the user accepts the delete dialog from this activity, or
     * 2. when the user has confirmed deleting the entry from the update activity.
     * */
    private void deleteEntryAndGoToDashboard() {
        Intent replyIntent = new Intent();
        replyIntent.putExtra("id", mViewModel.getEntryId());
        replyIntent.putExtra("position", mGalleryAdapterPosition);

        mViewModel.deleteEntry(mViewModel.getEntryId());
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
