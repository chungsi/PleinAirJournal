package com.example.pleinairjournal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class NewEntryActivity extends NewEntryMenu implements View.OnClickListener {
    private SharedPreferences sharedPrefs;
    private static final int REQUEST_TAKE_PHOTO = 4742;
    private NewEntryViewModel mViewModel;
    private CompassViewModel mCompassViewModel;

    TextView text_timetamp, text_viewCardinal, text_setCardinal;
    EditText edit_location, edit_comment;
    AutoCompleteTextView autoComplete_location;
    Button button_createEntry, button_takePhoto, button_setCardinal;
    ImageView image_photoThumb;

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

        setContentView(R.layout.activity_new_entry);

        mViewModel = ViewModelProviders.of(this).get(NewEntryViewModel.class);

        button_setCardinal = findViewById(R.id.button_setCardinal);
        button_setCardinal.setOnClickListener(this);
        button_takePhoto = findViewById(R.id.button_takePhoto);
        button_takePhoto.setOnClickListener(this);

        initCommentField();
        initLocationField();

        image_photoThumb = findViewById(R.id.image_photoThumbnail);

        text_timetamp = findViewById(R.id.text_timestamp);
        text_timetamp.setText(String.valueOf(mViewModel.getTimestamp()));
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

        // Buttons for toolbar
        button_cancel = findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);
        button_createEntry = findViewById(R.id.button_createEntry);
        button_createEntry.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
//        super.onClick(view);

        if (view.equals(button_cancel)) {
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }
        // this buttons are instantiated in the NewEntryMenu, but since they need the viewModel,
        // not so sure how best to abstract it out yet...
        if (view.equals(button_createEntry)) {
            // send data to a data filtering/cleaning function first, to return a status code?
            // then can see if something is left blank that shouldn't be
            long id = mViewModel.insertEntry();

            if (id != -1) {
                Toast.makeText(this, "Added entry!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, GalleryActivity.class);
                startActivity(i);
            }
        } else if (view.equals(button_takePhoto)) {
            startCameraActivity();
        } else if (view.equals(button_setCardinal)) {
            Log.i("PLEINAIR_DEBUG", "Set cardinal clicked: " + mCompassViewModel.compassLiveData.getCardinalMessage());
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
            Log.i("PLEINAIR_DEBUG", "onAcitivityResult successfull");
            displayImagePreview();
        }
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
                // catch exception if image can't be taken
                Log.d("PLEINAIR_DEBUG", e.getMessage());
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
//        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//        bmpFactoryOptions.inJustDecodeBounds = false;
//
//        // Need to decode the image from a file path because the camera intent will put
//        // the data into the URI passed to it
//        Bitmap bmp = BitmapFactory.decodeFile(mViewModel.imageFilePath, bmpFactoryOptions);
//        image_photoThumb.setImageBitmap(bmp);

        Glide.with(this)
                .load(mViewModel.getImageFilePath())
                .into(image_photoThumb);
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
