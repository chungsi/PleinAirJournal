package com.example.pleinairjournal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private JournalDb mDb;
    private NewEntryViewModel mViewModel;

    private String currentPhotoPath;

    TextView text_timetamp;
    EditText edit_location, edit_comment;
    Button button_createNewEntry, button_takePhoto;
    ImageView image_photoThumb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        mViewModel = ViewModelProviders.of(this).get(NewEntryViewModel.class);

        edit_location = findViewById(R.id.edit_location);
        edit_comment = findViewById(R.id.edit_comment);
        button_createNewEntry = findViewById(R.id.button_createNewEntry);
        button_createNewEntry.setOnClickListener(this);
        button_takePhoto = findViewById(R.id.button_takePhoto);
        button_takePhoto.setOnClickListener(this);
        image_photoThumb = findViewById(R.id.image_photoThumbnail);

        text_timetamp = findViewById(R.id.text_timestamp);
        text_timetamp.setText(String.valueOf(mViewModel.getTimestamp()));

        addTextChangeListeners();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(button_createNewEntry)) {
            // send data to a data filtering/cleaning function first, to return a status code?
            // then can see if something is left blank that shouldn't be
            long id = mViewModel.insertEntry();

            if (id != -1) {
                Toast.makeText(this, "Added entry!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, GalleryActivity.class);
                startActivity(i);
            }
        }
        if (view.equals(button_takePhoto)) {
            startCameraActivity();
        }
    }

    protected void startCameraActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image_photoThumb.setImageBitmap(imageBitmap);
        }
    }

    /**
     * Adds a textChangeListener to the EditTexts, so that the value is changed as the user types.
     * This way, stuff isn't lost when the screen is rotated.
     * */
    private void addTextChangeListeners() {
        edit_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mViewModel.location = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        edit_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mViewModel.comment = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
