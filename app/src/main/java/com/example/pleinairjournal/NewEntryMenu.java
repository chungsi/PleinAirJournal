package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewEntryMenu extends AppCompatActivity implements View.OnClickListener {

    Button button_cancel, button_createEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry_menu);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar_newEntry);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
//            toolbar.setNavigationIcon(R.drawable.icon_home);
            getSupportActionBar().setTitle("");
        }


        button_cancel = findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);
        button_createEntry = findViewById(R.id.button_createEntry);
        button_createEntry.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(button_cancel)) {
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }

    }
}
