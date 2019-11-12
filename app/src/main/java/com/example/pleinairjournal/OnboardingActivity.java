package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class OnboardingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit_name;
    Button button_saveName;
    SharedPreferences sharedPrefs;
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        //changes background image
        LinearLayout linearLayout_background = (LinearLayout) findViewById(R.id.linearLayout_background);
        linearLayout_background.setBackgroundResource(R.drawable.ic_bg_onboarding);

        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        // Test function below call to clear SharedPrefs to show the onboarding module!
//        editor.clear().apply();

        //checks if user has entered name at onboarding already, skips onboarding if they have
        boolean isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);
        if(isLoggedIn){
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }

        //creates an array for 3 images in drawable folder
        imageView2 = findViewById(R.id.imageView2);
        final int[] imageArray = { R.drawable.ic_onboarding1, R.drawable.ic_onboarding2,
                R.drawable.ic_onboarding3 };

        //runs the 3 images in array to display one at a time w/delay in ImageView
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                imageView2.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 1500);
            }
        };
        handler.postDelayed(runnable, 1500);

        edit_name = findViewById(R.id.edit_name);
        button_saveName = findViewById(R.id.button_saveName);
        button_saveName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(button_saveName)) {
            // saves name to shared preferences, then goes to dashboard activity
            sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("USERNAME", edit_name.getText().toString());
            sharedPrefs.edit().putBoolean("isLoggedIn", true).apply();
            editor.apply();

            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }
    }
}
