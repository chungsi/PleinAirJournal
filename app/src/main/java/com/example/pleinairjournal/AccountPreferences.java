package com.example.pleinairjournal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AccountPreferences extends JournalMenu implements View.OnClickListener {

    SharedPreferences sharedPrefs;
    TextView text_name;
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

        boolean darkModeChecked = sharedPrefs.getBoolean("DARKBUTTONCHECKED", false);
        if(darkModeChecked){
            setTheme(R.style.style_dark);
            Toast.makeText(this, "Dark Mode Checked", Toast.LENGTH_SHORT).show();
        }

        boolean lightModeChecked = sharedPrefs.getBoolean("LIGHTBUTTONCHECKED", false);
        if(lightModeChecked){
            setTheme(R.style.AppTheme);
            Toast.makeText(this, "Light Mode Checked", Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_account_preferences);

        text_name = findViewById(R.id.text_name);
        String username = sharedPrefs.getString("USERNAME", "");
        text_name.setText(username);

        radioButton_light = findViewById(R.id.radioButton_light);
        radioButton_dark = findViewById(R.id.radioButton_dark);

        button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(this);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("LIGHTBUTTONCHECKED", radioButton_light.isChecked());
                editor.putBoolean("DARKBUTTONCHECKED", radioButton_dark.isChecked());
                editor.commit();
                AccountPreferences.this.recreate();
            }
        });

        //Buttons from Toolbar
        imageButton_home = findViewById(R.id.imageButton_home);
        imageButton_home.setOnClickListener(this);
        imageButton_gallery = findViewById(R.id.imageButton_gallery);
        imageButton_gallery.setOnClickListener(this);
        imageButton_settings = findViewById(R.id.imageButton_settings);
        imageButton_settings.setOnClickListener(this);
        imageButton_create = findViewById(R.id.imageButton_create);
        imageButton_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //MENU BAR BUTTONS
        if(view.equals(imageButton_home)){
            Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(i);
        }
        if(view.equals(imageButton_gallery)){
            Intent i = new Intent(getApplicationContext(), GalleryActivity.class);
            startActivity(i);
        }

        if(view.equals(imageButton_settings)){
            Intent i = new Intent(getApplicationContext(), AccountPreferences.class);
            startActivity(i);
        }
        if(view.equals(imageButton_create)){
            Intent i = new Intent(getApplicationContext(), NewEntryActivity.class);
            startActivity(i);
        }
    }



}
