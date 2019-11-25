package com.example.pleinairjournal;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import static android.content.Context.SENSOR_SERVICE;

public class CompassViewModel extends AndroidViewModel {
    public CompassLiveData compassLiveData = new CompassLiveData();

    public CompassViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * A LiveData object for the compass data, that can be reused in views.
     * */
    public class CompassLiveData extends LiveData<String> implements SensorEventListener {
        private float[] mGravity = new float[3];
        private float[] mGeomagnetic = new float[3];
        private float[] mOrientation = new float[3];
        private float[] mRotationMatrix = new float[9];
        private float[] mInclinationMatrix = new float[9];
        private float mAzimut;

        private SensorManager mSensorManager;
        private Sensor mCompass, mAccel;

        public CompassLiveData() {
            mSensorManager = (SensorManager) getApplication().getSystemService(SENSOR_SERVICE);
            mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mCompass= mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            final float alpha = 0.97f;

            // Apply an alpha filtering to accelerometer values to isolate gravity
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity = event.values;
//                for (int i = 0; i < event.values.length; i++) {
//                    mGravity[i] = alpha * mGravity[i] + (1 - alpha) * event.values[i];
//                }
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic = event.values;
            }

            // Get the orientation values to convert into cardinal values
            if (mGravity != null && mGeomagnetic != null
                    && SensorManager.getRotationMatrix(mRotationMatrix, mInclinationMatrix, mGravity, mGeomagnetic)) {

                // mOrientation contains azimut, pitch and roll
                SensorManager.getOrientation(mRotationMatrix, mOrientation);
                mAzimut = mOrientation[0];

                postValue(getCardinalMessage()); // posts value to the LiveData object
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) { }

        public String getCardinalMessage() {
            return getCardinalDegree() + "° " + getCardinalDirection();
        }

        /**
         * Gets cardinal direction from last saved azimut value from sensor readings.
         * */
        public String getCardinalDirection() {
            float degrees = getCardinalDegree(mAzimut);
            return getCardinalDirection(degrees);
        }

        /**
         * Get the rotation degree (0-360°) based on last saved orientation.
         * */
        public int getCardinalDegree() {
            return Math.round(getCardinalDegree(mAzimut));
        }

        /**
         * Private method to get the rotation degree from inputted radians.
         * */
        protected float getCardinalDegree(float rad) { return ((float) Math.toDegrees(rad) + 360) % 360; }

        protected String getCardinalDirection(float degree) {
            if (degree >= 337.5) return "N";
            else if (degree >= 292.5) return "NW";
            else if (degree >= 247.5) return "W";
            else if (degree >= 202.5) return "SW";
            else if (degree >= 157.5) return "S";
            else if (degree >= 112.5) return "SE";
            else if (degree >= 67.5) return "E";
            else if (degree >= 22.5) return "NE";
            else return "N";
        }

        @Override
        protected void onActive() {
            super.onActive();
            if (mAccel != null) {
                mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
            }

            if(mCompass != null){
                mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            mSensorManager.unregisterListener(this);
        }
    }
}
