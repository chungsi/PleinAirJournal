package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_goToAddEntry, button_goToGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_goToAddEntry = findViewById(R.id.button_goToAddEntry);
        button_goToGallery = findViewById(R.id.button_goToGallery);

        button_goToAddEntry.setOnClickListener(this);
        button_goToGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(button_goToAddEntry)) {
            Intent i = new Intent(this, NewEntryActivity.class);
            startActivity(i);
        }

        if (view.equals(button_goToGallery)) {
            Intent i = new Intent(this, GalleryActivity.class);
            startActivity(i);
        }
    }
}
