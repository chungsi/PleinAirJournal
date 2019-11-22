package com.example.pleinairjournal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ViewEntryMenu extends MasterActivity {
    static final int UPDATE_ENTRY = 2;

    private ImageButton button_back, button_deleteEntry;
    private Button button_updateEntry;

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
    public void initMenuButtons(final ViewEntryViewModel mViewModel){
        button_back = findViewById(R.id.button_back);
        button_updateEntry = findViewById(R.id.button_updateEntry);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_updateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UpdateEntryActivity.class);
                i.putExtra("id", mViewModel.getEntryId());
                Log.i("PLEINAIR_DEBUG", "intent id from ViewEntry: " + mViewModel.getEntryId());
                startActivityForResult(i, UPDATE_ENTRY);
            }
        });
    }

    public void initMenuButtons(final UpdateEntryViewModel mViewModel) {
        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void initMenuDeleteButton(final ViewEntryViewModel mViewModel, final int mGalleryAdapterPosition) {
        button_deleteEntry = findViewById(R.id.button_deleteEntry);

        button_deleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.deleteEntry(mViewModel.getEntryId());

                Intent replyIntent = new Intent();
                replyIntent.putExtra("id", mViewModel.getEntryId());
                replyIntent.putExtra("position", mGalleryAdapterPosition);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }
}
