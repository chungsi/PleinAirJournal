package com.example.pleinairjournal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class AccountPreferences extends JournalMenu implements View.OnClickListener {

    SharedPreferences sharedPrefs;
    EditText editText_name;
    RadioGroup radioGroup_colourScheme;
    RadioButton radioButton_light, radioButton_dark;
    Button button_save;
    ImageButton imageButton_home, imageButton_gallery, imageButton_settings, imageButton_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checks which theme the user has selected
        //Theme can only be changed before setContentView
        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        //checks if user has selected dark colour scheme preference, sets theme if yes
        boolean darkModeChecked = sharedPrefs.getBoolean("DARKBUTTONCHECKED", false);
        if(darkModeChecked){
            setTheme(R.style.style_dark);
        }

        //checks if user has selected light colour scheme preference, sets theme if yes
        boolean lightModeChecked = sharedPrefs.getBoolean("LIGHTBUTTONCHECKED", false);
        if(lightModeChecked){
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_account_preferences);

        editText_name = findViewById(R.id.editText_name);
        String username = sharedPrefs.getString("USERNAME", "");
        editText_name.setText(username);

        radioButton_light = findViewById(R.id.radioButton_light);
        radioButton_dark = findViewById(R.id.radioButton_dark);

        button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(this);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("USERNAME", editText_name.getText().toString());
                editor.putBoolean("LIGHTBUTTONCHECKED", radioButton_light.isChecked());
                editor.putBoolean("DARKBUTTONCHECKED", radioButton_dark.isChecked());
                editor.commit();
                AccountPreferences.this.recreate();
            }
        });

        findMenuButtons();

        //Changes colour of icon for current page
        imageButton_settings = findViewById(R.id.imageButton_settings);
        imageButton_settings.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
    }



}
