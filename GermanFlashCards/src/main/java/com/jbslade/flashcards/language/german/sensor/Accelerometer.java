package com.jbslade.flashcards.language.german.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import com.jbslade.flashcards.language.german.debug.MyLogger;

public class Accelerometer implements SensorEventListener
{
    private static final int DELAY_SENSOR_GRAVITY_CALIBRATION = 500;
    public static final float MAX_THRESHOLD = 29.42f; //3 Gs
    public static final float DEFAULT_THRESHOLD = (MAX_THRESHOLD / 5); //.75 Gs
    public boolean isActive = false;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private OnThresholdPassedListener ThresholdPassedListener;
    private OnAccelValuesListener AccelValuesListener;

    private boolean isSensorCalibrated = false;
    private boolean initialSensorReading = true;
    private float gravity[] = {0, 0, 0};
    private float linearAccel[] = {0, 0, 0};

    // debugging
    // private int holdAccelX[];
    // private int holdAccelY[];
    // private int holdAccelZ[];
    // private int threshholdCount = 0;

    public Accelerometer(Context context)
    {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Debug
        // initFindRange();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        MyLogger.logSevere(Accelerometer.class.getSimpleName(), "onAccuracyChanged",
                "Accuracy of accelerometer has changed: " + accuracy);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (initialSensorReading)
        {
            delayUntilCalibrated();
            initialSensorReading = false;
        }

        float alpha = .8f;
        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linearAccel[0] = event.values[0] - gravity[0];
        linearAccel[1] = event.values[1] - gravity[1];
        linearAccel[2] = event.values[2] - gravity[2];

        if (isSensorCalibrated)
        {
            if (isPastThreshhold() && ThresholdPassedListener != null)
            {
                ThresholdPassedListener.onThresholdPassed();
            }

            if (AccelValuesListener != null)
            {
                AccelValuesListener
                        .OnNewAccelValues(linearAccel[0], linearAccel[1], linearAccel[2]);
            }
        }

        // Debug
        // findRangeOfAccel();
    }

    public void reactivateAccelerometer()
    {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        initialSensorReading = true;
        isSensorCalibrated = false;
        isActive = true;
    }

    public void deactivateAccelerometer()
    {
        mSensorManager.unregisterListener(this);
        isActive = false;
    }

    private boolean isPastThreshhold()
    {
        if (Math.abs(linearAccel[0]) > DEFAULT_THRESHOLD ||
                Math.abs(linearAccel[1]) > DEFAULT_THRESHOLD
                || Math.abs(linearAccel[2]) > DEFAULT_THRESHOLD)
            return true;
        return false;
    }

    // Literally delays a certain amount of time such that the accelerometer has
    // time to negate gravity.
    private void delayUntilCalibrated()
    {
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                isSensorCalibrated = true;
            }

        }, DELAY_SENSOR_GRAVITY_CALIBRATION);
    }

    // private void initFindRange()
    // {
    // holdAccelX = new int[50];
    // holdAccelY = new int[50];
    // holdAccelZ = new int[50];
    // for(int i = 0; i < 50; i++)
    // {
    // holdAccelX[i] = 0;
    // holdAccelY[i] = 0;
    // holdAccelZ[i] = 0;
    // }
    // }
    //
    // private void findRangeOfAccel()
    // {
    // if(isSensorCalibrated)
    // {
    // if(Math.abs(linearAccel.x) > ACCEL_THRESHHOLD ||
    // Math.abs(linearAccel.y) > ACCEL_THRESHHOLD ||
    // Math.abs(linearAccel.z) > ACCEL_THRESHHOLD)
    // threshholdCount++;
    //
    // holdAccelX[Math.abs((int)linearAccel.x)]++;
    // holdAccelY[Math.abs((int)linearAccel.y)]++;
    // holdAccelZ[Math.abs((int)linearAccel.z)]++;
    //
    // if (threshholdCount == 50)
    // {
    //
    // threshholdCount++;
    // }
    // }
    // }

    public interface OnThresholdPassedListener
    {
        public void onThresholdPassed();
    }

    public void setOnThresholdPassedListener(OnThresholdPassedListener threshList)
    {
        ThresholdPassedListener = threshList;
    }

    // To keep from using another progress bar in this class, we will have this
    // interface that will return modified values only after the accelerometer
    // is calibrated. My goal is to abstract this class so that no particular
    // progress bar is tied to this class.
    public interface OnAccelValuesListener
    {
        public void OnNewAccelValues(float x, float y, float z);
    }

    public void setOnValuesListener(OnAccelValuesListener newListener)
    {
        AccelValuesListener = newListener;
    }
}
