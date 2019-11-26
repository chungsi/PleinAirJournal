package com.example.pleinairjournal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ViewEntryMenu extends MasterActivity {

    private ImageButton button_back, button_deleteEntry;
    private Button button_updateEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.applyColourScheme();
        setContentView(R.layout.menu_view_entry);

        super.initToolbar();
    }

    /**
     * Public function that must be called in child activities to instantiate the buttons.
     * */
    public void initMenuButtons(){
        button_back = findViewById(R.id.button_back);
        button_updateEntry = findViewById(R.id.button_updateEntry);
        button_deleteEntry = findViewById(R.id.button_deleteEntry);
    }

    /**
     * For the specific views that use similar buttons, but the text will be somewhat different.
     * */
    public void initMenuButtonsFor(String activity) {
        if (activity.equals("view")) {
            initMenuButtons();
        } else if (activity.equals("update")) {
            initMenuButtons();
            button_updateEntry.setText(R.string.button_save_update_entry);
        }
    }

    /**
     * The update button must have their own click listeners instantiated in the activity itself.
     * */
    public Button getMenuUpdateButton() {
        return button_updateEntry;
    }

    /**
     * The delete button must have their own click listeners instantiated in the activity itself.
     * */
    public ImageButton getMenuDeleteButton() {
        return button_deleteEntry;
    }

    /**
     * The back button must have their own click listeners instantiated in the activity.
     * */
    public ImageButton getMenuBackButton() { return button_back; }
}
