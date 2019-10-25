package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OnboardingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit_name;
    Button button_saveName;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);


        boolean isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);

        if(isLoggedIn){
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }

        edit_name = findViewById(R.id.edit_name);
        button_saveName = findViewById(R.id.button_saveName);
        button_saveName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(button_saveName)) {
            // saves name to shared preferences
            sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("USERNAME", edit_name.getText().toString());
            sharedPrefs.edit().putBoolean("isLoggedIn", true).commit();
            editor.commit();

            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }
    }
}
