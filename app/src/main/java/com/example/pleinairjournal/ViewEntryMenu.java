package com.example.pleinairjournal;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ViewEntryMenu extends MasterActivity {

    ImageButton button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_view_entry);

        super.initToolbar();
        super.applyColourScheme();
    }

    /**
     * Public function that must be called in child activities to instantiate the buttons.
     * */
    public void initMenuButtons(){
        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
