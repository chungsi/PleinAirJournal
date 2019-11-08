package com.example.pleinairjournal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

public class Compass implements SensorEventListener {

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float[] mOrientation = new float[3];
    private float mAzimut, rotation, cardinalDegree;
    private float[] rotationMatrix = new float[9];
    private float[] inclinationMatrix = new float[9];
    private String mCardinalDirection = "North";

    private SensorManager mSensorManager;
    private Sensor mTemperature, mCompass, mAccel;

    public Compass(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mCompass= mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        if (mAccel != null) {
            mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(mCompass != null){
            mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            mGravity = event.values;
            for (int i = 0; i < event.values.length; i++) {
                mGravity[i] = alpha * mGravity[i] + (1 - alpha) * event.values[i];
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values;
        }

        if (mGravity != null && mGeomagnetic != null
                && SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, mGravity, mGeomagnetic)) {

            // mOrientation contains azimut, pitch and roll
            SensorManager.getOrientation(rotationMatrix, mOrientation);

            mAzimut = mOrientation[0];
            Log.i("PLEINAIR_DEBUG", "mAzimut: " + mAzimut
                    + "; mAzimut to degrees: " + getCardinalDegree(mAzimut)
                    + "; cardinal: " + getCardinalDirection(getCardinalDegree(mAzimut))
                    + "; inclination: " + SensorManager.getInclination(inclinationMatrix));

//            text_compass.setText("You're facing \n"
//                    + Math.round(getCardinalDegree(mAzimut)) + "° "
//                    + getCardinalDirection());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    public String getCardinalMessage() {
        return getCardinalDegree() + "° " + getCardinalDirection();
    }

    public String getCardinalDirection() {
        float degrees = getCardinalDegree(mAzimut);
        mCardinalDirection = getCardinalDirection(degrees);
        return mCardinalDirection;
    }

    public int getCardinalDegree() {
        return Math.round(getCardinalDegree(mAzimut));
    }

    private float getCardinalDegree(float rad) {
        return ((float) Math.toDegrees(rad) + 360) % 360;
    }

    private String getCardinalDirection(float degree) {
        if (degree >= 337.5) return "N";
        else if (degree >= 292.5) return "NW";
        else if (degree >= 247.5) return "W";
        else if (degree >= 202.5) return "SW";
        else if (degree >= 157.5) return "S";
        else if (degree >= 112.5) return "SE";
        else if (degree >= 67.5) return "E";
        else if (degree >= 22.5) return "NE";
        else return "North";
    }

//    private void updateOrientationAngles() {
//        SensorManager.getRotationMatrix(R, null,
//                mGravity, mGeomagnetic);
//        SensorManager.getOrientation(R, I);
//    }
}
