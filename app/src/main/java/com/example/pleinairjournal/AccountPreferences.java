package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class AccountPreferences extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPrefs;
    TextView text_name;
    Spinner spinner_colourScheme, spinner_timeZone;
    Button button_save;

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

        spinner_timeZone = findViewById(R.id.spinner_timeZone);
        ArrayAdapter<CharSequence> adapter_timeZone = ArrayAdapter.createFromResource(this,
                R.array.array_timeZone, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter_timeZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner_timeZone.setAdapter(adapter_timeZone);

        button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(this);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData",MODE_PRIVATE);
        int spinnerTimeZone = sharedPrefs.getInt("SELECTEDTIMEZONE",-1);
        if(spinnerTimeZone != -1) {
            // set the selected value of the spinner
            spinner_timeZone.setSelection(spinnerTimeZone);
        }

        int spinnerColourScheme = sharedPrefs.getInt("SELECTEDCOLOURSCHEME",-1);
        if(spinnerColourScheme != -1) {
            // set the selected value of the spinner
            spinner_colourScheme.setSelection(spinnerColourScheme);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.equals(button_save)) {
            int selectedTimeZone = spinner_timeZone.getSelectedItemPosition();
            int selectedColourScheme = spinner_colourScheme.getSelectedItemPosition();
            sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putInt("SELECTEDTIMEZONE", selectedTimeZone);
            editor.putInt("SELECTEDCOLOURSCHEME", selectedColourScheme);
            editor.commit();

        }
    }
}
