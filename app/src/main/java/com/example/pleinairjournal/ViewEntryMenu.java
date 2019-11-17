package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ViewEntryMenu extends AppCompatActivity implements View.OnClickListener{

    ImageButton imageButton_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry_menu);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar_viewEntry);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(""); //gets rid of default title
        }
    }

    //connects to buttons in XML, listens for clicks
    public void findMenuButtons(){
        imageButton_back = findViewById(R.id.imageButton_back);
        imageButton_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //Checks if tool bar buttons have been pressed
        if (view.equals(imageButton_back)) {
            finish();
        }
    }
}
