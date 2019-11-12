package com.example.pleinairjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class JournalMenu extends AppCompatActivity implements View.OnClickListener{

    ImageButton imageButton_home, imageButton_gallery, imageButton_settings, imageButton_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.mToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
//            toolbar.setNavigationIcon(R.drawable.icon_home);
            getSupportActionBar().setTitle("");
        }

    }


    public void findMenuButtons(){
        imageButton_home = findViewById(R.id.imageButton_home);
        imageButton_home.setOnClickListener(this);
        imageButton_gallery = findViewById(R.id.imageButton_gallery);
        imageButton_gallery.setOnClickListener(this);
        imageButton_settings = findViewById(R.id.imageButton_settings);
        imageButton_settings.setOnClickListener(this);
        imageButton_create = findViewById(R.id.imageButton_create);
        imageButton_create.setOnClickListener(this);
    }

    // JournalMenu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        Log.i("iris", "onCreateOptionsMenuCalled");
//        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {

        //MENU BAR BUTTONS
        if (view.equals(imageButton_home)) {
            Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(i);
        }
        if (view.equals(imageButton_gallery)) {
            Intent i = new Intent(getApplicationContext(), GalleryActivity.class);
            startActivity(i);
        }

        if (view.equals(imageButton_settings)) {
            Intent i = new Intent(getApplicationContext(), AccountPreferences.class);
            startActivity(i);
        }
        if (view.equals(imageButton_create)) {
            Intent i = new Intent(getApplicationContext(), NewEntryActivity.class);
            startActivity(i);
        }

    }
}
