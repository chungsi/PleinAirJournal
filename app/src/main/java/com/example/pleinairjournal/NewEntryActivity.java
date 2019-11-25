package com.example.pleinairjournal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NewEntryActivity extends NewEntryMenu implements View.OnClickListener {
    private static final int REQUEST_TAKE_PHOTO = 4742;
    private NewEntryViewModel mViewModel;
    private CompassViewModel mCompassViewModel;
    private boolean mHasTakenFirstPhoto = false;

    TextView text_viewCardinal, text_setCardinal;
    EditText edit_comment;
    AutoCompleteTextView autoComplete_location;
    Button button_takePhoto, button_setCardinal;
    ImageView image_photoThumb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        super.initMenuButtons();
        initCreateEntryButton();

        mViewModel = ViewModelProviders.of(this).get(NewEntryViewModel.class);

        button_setCardinal = findViewById(R.id.button_setCardinal);
        button_setCardinal.setOnClickListener(this);
        button_takePhoto = findViewById(R.id.button_takePhoto);
        button_takePhoto.setOnClickListener(this);

        initImagePreview();
        initCommentField();
        initLocationField();

        text_viewCardinal = findViewById(R.id.text_viewCardinal);
        text_setCardinal = findViewById(R.id.text_setCardinal);

        // ViewModel for the compass
        mCompassViewModel = ViewModelProviders.of(this).get(CompassViewModel.class);
        mCompassViewModel.compassLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                text_viewCardinal.setText(s);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.equals(button_takePhoto)) {
            startCameraActivity();
        } else if (view.equals(button_setCardinal)) {
            text_setCardinal.setText(mCompassViewModel.compassLiveData.getCardinalMessage());

            // sets the cardinal direction and degree of the new entry in the viewModel
            mViewModel.setCardinal(mCompassViewModel.compassLiveData.getCardinalDirection());
            mViewModel.setCardinalDegree(mCompassViewModel.compassLiveData.getCardinalDegree());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            displayImagePreview();
        }
    }

    private void initCreateEntryButton() {
        super.getCreateEntryButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areAllRequiredFieldsCheckedOut()) {
                    mViewModel.insertEntry();
                    Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplication(), R.string.prompt_create_entry_error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
    * Checks that the required fields are filled out.
    * Namely, there is a photo and a location input. Other fields are voluntary.
    * */
    private boolean areAllRequiredFieldsCheckedOut() {
        if (!mHasTakenFirstPhoto && mViewModel.getImageFilePath().isEmpty()) {
            return false;
        }
        if (autoComplete_location.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Starts the camera intent if a camera function is available.
     * */
    protected void startCameraActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = mViewModel.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }

    /**
     * Displays the photo preview from the camera intent by using the saved image filepath.
     * */
    private void displayImagePreview() {
        handleFirstPhotoTaken();

        Glide.with(this)
                .load(mViewModel.getImageFilePath())
                .into(image_photoThumb);
    }

    /**
     * Handles the case after the first photo has been taken, because the image preview will be
     * updated. The button is also moved, and the text inside changes.
     * */
    private void handleFirstPhotoTaken() {
        if (!mHasTakenFirstPhoto) {
            mHasTakenFirstPhoto = true;
            resetImagePreviewAndButtonConstraints();

            button_takePhoto.setText(R.string.button_retake_photo);
        }
    }

    /**
     * The ratio of the image preview has been set to be a square when no photo has been taken yet.
     * This function can be called to reset the ratio and have the preview fit the image's real
     * dimensions.
     * */
    private void resetImagePreviewAndButtonConstraints() {
        ConstraintSet c = new ConstraintSet();
        ConstraintLayout mConstraint = findViewById(R.id.constraint_newEntryImagePreview);
        c.clone(mConstraint);

        c.setDimensionRatio(R.id.image_newPhotoPreview, "");
        c.constrainHeight(R.id.image_newPhotoPreview, ConstraintSet.WRAP_CONTENT);

        // Sets button to be on the bottom right of the photo preview
        c.removeFromHorizontalChain(R.id.button_takePhoto);
        c.removeFromVerticalChain(R.id.button_takePhoto);
        c.connect(R.id.button_takePhoto, ConstraintSet.BOTTOM,
                R.id.image_newPhotoPreview, ConstraintSet.BOTTOM,
                16);
        c.connect(R.id.button_takePhoto, ConstraintSet.RIGHT,
                R.id.image_newPhotoPreview, ConstraintSet.RIGHT,
                16);

        c.applyTo(mConstraint);
    }

    private void initImagePreview() {
        image_photoThumb = findViewById(R.id.image_newPhotoPreview);
    }

    private void initCommentField() {
        edit_comment = findViewById(R.id.edit_comment);
        edit_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mViewModel.setComment(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
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

        // textChangeListener to save to ViewModel
        autoComplete_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mViewModel.setLocation(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
