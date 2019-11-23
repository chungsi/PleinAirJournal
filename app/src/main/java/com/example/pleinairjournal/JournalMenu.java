package com.example.pleinairjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class JournalMenu extends MasterActivity {

    ImageButton button_home, button_settings, button_create;
    TextView text_compassDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.applyColourScheme();
        setContentView(R.layout.menu_default);

        super.initToolbar();
    }

    /**
     * @param act is the string of the active activity. Accepted values are: gallery, dashboard,
     *            and settings.
     *
     * Public method to be called that sets one of the items as the active activity by changing the
     * icon colour.
     * */
    public void initMenuButtonsWithActive(String act) {
        initMenuButtons();

        if (act.equals("dashboard")) {
            button_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        } else if (act.equals("settings")) {
            button_settings.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        }
    }

    /**
     * Initializes the compass display layout in the toolbar by setting it to visible.
     * Also binds the TextView for inputting the compass coordinates.
     * */
    public void initCompassDisplay() {
        LinearLayout layout_compass = findViewById(R.id.layout_toolbarCompass);
        layout_compass.setVisibility(View.VISIBLE);

        text_compassDegree = findViewById(R.id.text_compassDegree);
    }

    public TextView getCompassTextView() {
        return text_compassDegree;
    }

    // JournalMenu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return true;
    }

    /**
     * Public function that must be called in child activities to instantiate the buttons.
     * */
    private void initMenuButtons(){
        button_settings = findViewById(R.id.button_settings);
        button_home = findViewById(R.id.button_home);
        button_create = findViewById(R.id.button_entryCreate);

        button_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0); //removes sliding animation
            }
        });
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AccountPreferences.class);
                startActivity(i);
                overridePendingTransition(0, 0); //removes sliding animation
            }
        });
        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewEntryActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0); //removes sliding animation
            }
        });
    }
}
