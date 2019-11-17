package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class ViewEntryMenu extends MasterActivity {

    ImageButton imageButton_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry_menu);

        super.initToolbar();
        super.applyColourScheme();
    }

    /**
     * Public function that must be called in child activities to instantiate the buttons.
     * */
    public void initMenuButtons(){
        imageButton_back = findViewById(R.id.imageButton_back);
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
