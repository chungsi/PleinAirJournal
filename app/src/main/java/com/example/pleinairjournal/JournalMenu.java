package com.example.pleinairjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class JournalMenu extends MasterActivity {

    ImageButton button_home, button_settings, button_create, button_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_default);

        super.initToolbar();
        super.applyColourScheme();
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
            button_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
        } else if (act.equals("settings")) {
            button_settings.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
        }
//      else if (act.equals("gallery")) {
//            button_gallery.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
//        }
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
        button_create = findViewById(R.id.button_create);
//        button_gallery = findViewById(R.id.button_gallery);

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
//        button_gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), GalleryActivity.class);
//                startActivity(i);
//                overridePendingTransition(0, 0); //removes sliding animation
//            }
//        });
    }
}
