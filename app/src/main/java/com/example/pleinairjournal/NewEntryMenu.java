package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NewEntryMenu extends MasterActivity {

    Button button_cancel, button_createEntry;

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
        button_createEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = mViewModel.insertEntry();

                if (id != -1) {
                    Toast.makeText(getApplicationContext(), "Added entry!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
