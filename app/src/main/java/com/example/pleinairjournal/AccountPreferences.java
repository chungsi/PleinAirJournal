package com.example.pleinairjournal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AccountPreferences extends JournalMenu {

    SharedPreferences sharedPrefs;
    EditText editText_name;
    RadioGroup radioGroup_colourScheme;
    RadioButton radioButton_light, radioButton_dark;
    Button button_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_preferences);
        super.initMenuButtonsWithActive("settings");

        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String username = sharedPrefs.getString("USERNAME", "");
        editText_name = findViewById(R.id.editText_name);
        editText_name.setText(username);

        initColourSchemeRadioButtons();
        initSaveButton();
    }

    private void initSaveButton() {
        button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("USERNAME", editText_name.getText().toString());
                editor.putBoolean("LIGHTBUTTONCHECKED", radioButton_light.isChecked());
                editor.putBoolean("DARKBUTTONCHECKED", radioButton_dark.isChecked());
                editor.apply();
                AccountPreferences.this.recreate();
            }
        });
    }

    private void initColourSchemeRadioButtons() {
        radioButton_light = findViewById(R.id.radioButton_light);
        radioButton_dark = findViewById(R.id.radioButton_dark);

        // Sets radio buttons based on saved values
        boolean isDarkButtonChecked = sharedPrefs.getBoolean("DARKBUTTONCHECKED", false);
        if (isDarkButtonChecked) radioButton_dark.setChecked(true);
    }
}
