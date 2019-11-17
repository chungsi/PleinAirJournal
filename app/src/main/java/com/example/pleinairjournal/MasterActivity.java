package com.example.pleinairjournal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MasterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Sets the Toolbar to act as the ActionBar for this Activity window.
     *
     * This function must be called before the child activity's applyColourScheme and setContentView.
     * */
    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(""); //gets rid of default title
        }
    }

    /**
     *  Sets the activity's colour scheme based on the saved SharedPreferences value.
     *
     *  This function must be called after the child activity's initToolbar and before setContentView.
     */
    public void applyColourScheme() {
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPrefs.edit();

        boolean darkModeChecked = sharedPrefs.getBoolean("DARKBUTTONCHECKED", false);
        boolean lightModeChecked = sharedPrefs.getBoolean("LIGHTBUTTONCHECKED", false);

        if (darkModeChecked){
            setTheme(R.style.style_dark);
        } else if (lightModeChecked){
            setTheme(R.style.AppTheme);
        }
    }
}
