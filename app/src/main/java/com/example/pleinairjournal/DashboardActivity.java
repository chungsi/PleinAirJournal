package com.example.pleinairjournal;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends JournalMenu {
    private CompassViewModel mCompassViewModel;
    private SharedPreferences sharedPrefs;
    TextView text_name, text_viewCardinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        super.initMenuButtonsWithActive("dashboard");

        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String username = sharedPrefs.getString("USERNAME", "");
        text_name = findViewById(R.id.text_name);
        text_name.setText(username + "!");

        text_viewCardinal = findViewById(R.id.text_compass);

        // Compass view model to display and update the cardinal direction view
        mCompassViewModel = ViewModelProviders.of(this).get(CompassViewModel.class);
        mCompassViewModel.compassLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                text_viewCardinal.setText("You're facing " + s);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

}
