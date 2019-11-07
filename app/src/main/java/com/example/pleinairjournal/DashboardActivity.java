package com.example.pleinairjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DashboardActivity extends JournalMenu implements SensorEventListener, View.OnClickListener {

    private SharedPreferences sharedPrefs;
    TextView text_name, text_ambientTemp, text_compass;
    private SensorManager mSensorManager;
    private Sensor mTemperature, mCompass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        text_name = findViewById(R.id.text_name);
        String username = sharedPrefs.getString("USERNAME", "");
        text_name.setText(username + "!");

        text_ambientTemp = findViewById(R.id.text_ambientTemp);
        text_compass = findViewById(R.id.text_compass);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mTemperature= mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mCompass= mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        findMenuButtons();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mTemperature != null) {
            mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(mCompass != null){
            mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float ambientTemp = sensorEvent.values[0];
        text_ambientTemp.setText("Current Temp.:\n " + String.valueOf(ambientTemp) + getResources().getString(R.string.celsius));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

//    public void tempButtonsForTesting() {
//
//        Button button_goToAddEntry = findViewById(R.id.button_goToAddEntry);
//        Button button_goToGallery = findViewById(R.id.button_goToGallery);
//
//        button_goToAddEntry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(view.getContext(), NewEntryActivity.class);
//                // This flag means NewEntryActivity won't be reachable by the back button once the
//                // activity is finished (a new entry is created, or that action is exited)
////                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                startActivityForResult(i, RESULT_OK);
//            }
//        });
//
//        button_goToGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(view.getContext(), GalleryActivity.class);
//                startActivity(i);
//            }
//        });
//    }

}
