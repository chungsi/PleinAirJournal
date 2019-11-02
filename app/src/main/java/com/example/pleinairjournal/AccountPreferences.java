package com.example.pleinairjournal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class AccountPreferences extends JournalMenu implements View.OnClickListener {

    private SharedPreferences sharedPrefs;
    TextView text_name;
    Spinner spinner_colourScheme;
    Button button_save;
    ImageButton imageButton_home, imageButton_gallery, imageButton_settings, imageButton_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_preferences);

        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        text_name = findViewById(R.id.text_name);
        String username = sharedPrefs.getString("USERNAME", "");
        text_name.setText(username);

        spinner_colourScheme = findViewById(R.id.spinner_colourScheme);
        ArrayAdapter<CharSequence> adapter_colourScheme = ArrayAdapter.createFromResource(this,
                R.array.array_colourScheme, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter_colourScheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner_colourScheme.setAdapter(adapter_colourScheme);


        button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(this);

        //Buttons from Toolbar
        imageButton_home = findViewById(R.id.imageButton_home);
        imageButton_home.setOnClickListener(this);
        imageButton_gallery = findViewById(R.id.imageButton_gallery);
        imageButton_gallery.setOnClickListener(this);
        imageButton_settings = findViewById(R.id.imageButton_settings);
        imageButton_settings.setOnClickListener(this);
        imageButton_create = findViewById(R.id.imageButton_create);
        imageButton_create.setOnClickListener(this);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData",MODE_PRIVATE);

        int spinnerColourScheme = sharedPrefs.getInt("SELECTEDCOLOURSCHEME",-1);
        if(spinnerColourScheme != -1) {
            // set the selected value of the spinner
            spinner_colourScheme.setSelection(spinnerColourScheme);
        }


    }

    @Override
    public void onClick(View view) {
        if (view.equals(button_save)) {
            int selectedColourScheme = spinner_colourScheme.getSelectedItemPosition();
            sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putInt("SELECTEDCOLOURSCHEME", selectedColourScheme);
            editor.commit();
        }

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
