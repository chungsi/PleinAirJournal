package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewEntryMenu extends MasterActivity {

    Button button_createEntry;
    ImageButton button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.applyColourScheme();
        setContentView(R.layout.menu_new_entry);

        super.initToolbar();
    }

    public void initMenuButtons() {
        // also access the newEntryViewModel
        final NewEntryViewModel mViewModel = ViewModelProviders.of(this).get(NewEntryViewModel.class);

        button_cancel = findViewById(R.id.button_entryCancel);
        button_createEntry = findViewById(R.id.button_entryCreate);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public Button getCreateEntryButton() {
        return button_createEntry;
    }
}
