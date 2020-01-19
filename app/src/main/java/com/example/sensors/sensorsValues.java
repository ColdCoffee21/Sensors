package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;

public class sensorsValues extends AppCompatActivity implements SensorEventListener {
    SensorManager mySensorManager;
    Sensor mAccelerometer, mLinearAccel, mGyro, mMagnetometer, mRotationVect, mStepCount, mStepDetect;

    //Accelerometer variables
    private float alastX, alastY, alastZ;
    private float adeltaX = 0, adeltaY = 0, adeltaZ = 0;
    private float avibrateThreshold = 0;
    private TextView acurrentX, acurrentY, acurrentZ;
    private Vibrator av;

    //linear acceleration variables
    private TextView linAccCurrentX, linAccCurrentY, linAccCurrentZ;

    //Gyroscope variables
    private TextView GyroCurrentX, GyroCurrentY, GyroCurrentZ;

    //orientation variables
    private TextView OrientCurrentX, OrientCurrentY, OrientCurrentZ;
    private final float[] accelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];
    private final float[] rotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    //magnetometer variables
    private TextView MagnetoCurrentX, MagnetoCurrentY, MagnetoCurrentZ;

    //rotation vector variables
    private TextView RotVectCurrentX, RotVectCurrentY, RotVectCurrentZ, RotVectScal;

    //Step counter variables
    private TextView sCount;
    int appSteps = 0;

    //Step detector variables
    private TextView sDetect;
    int sDetector = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_values);
        initializeViews();

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //accelerometer
        if (mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mySensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            avibrateThreshold = mAccelerometer.getMaximumRange() / 2;
        } else {
            acurrentX.setText("Nan");
            acurrentY.setText("Nan");
            acurrentZ.setText("Nan");
        }
        //linear acceleration
        if (mySensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) {
            mLinearAccel = mySensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mySensorManager.registerListener(this, mLinearAccel, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            linAccCurrentX.setText("Nan");
            linAccCurrentY.setText("Nan");
            linAccCurrentY.setText("Nan");
        }

        //Gyroscope
        if (mySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            mGyro = mySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mySensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            GyroCurrentX.setText("Nan");
            GyroCurrentY.setText("Nan");
            GyroCurrentZ.setText("Nan");
        }

        //Magnetometer
        if (mySensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            mMagnetometer = mySensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mySensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            MagnetoCurrentX.setText("Nan");
            MagnetoCurrentY.setText("Nan");
            MagnetoCurrentZ.setText("Nan");
        }

        //RotationVector
        if (mySensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
            mRotationVect = mySensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            mySensorManager.registerListener(this, mRotationVect, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            RotVectCurrentX.setText("Nan");
            RotVectCurrentY.setText("Nan");
            RotVectCurrentZ.setText("Nan");
            RotVectScal.setText("Nan");
        }

        //steps counter
        if (mySensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            mStepCount = mySensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mySensorManager.registerListener(this, mStepCount, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            sCount.setText("Nan");
        }

        //steps detector
        if (mySensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            mStepDetect = mySensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mySensorManager.registerListener(this, mStepDetect, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            sDetect.setText("Nan");
        }

    }
    public void initializeViews() {
        acurrentX = findViewById(R.id.accelCurrentX);
        acurrentY = findViewById(R.id.accelCurrentY);
        acurrentZ = findViewById(R.id.accelCurrentZ);

        linAccCurrentX = findViewById(R.id.LinAccelCurrentX);
        linAccCurrentY = findViewById(R.id.LinAccelCurrentY);
        linAccCurrentZ = findViewById(R.id.LinAccelCurrentZ);

        GyroCurrentX = findViewById(R.id.GyroCurrentX);
        GyroCurrentY = findViewById(R.id.GyroCurrentY);
        GyroCurrentZ = findViewById(R.id.GyroCurrentZ);

        OrientCurrentX = findViewById(R.id.OrientCurrentX);
        OrientCurrentY = findViewById(R.id.OrientCurrentY);
        OrientCurrentZ = findViewById(R.id.OrientCurrentZ);

        MagnetoCurrentX = findViewById(R.id.MagnetoCurrentX);
        MagnetoCurrentY = findViewById(R.id.MagnetoCurrentY);
        MagnetoCurrentZ = findViewById(R.id.MagnetoCurrentZ);

        RotVectCurrentX = findViewById(R.id.RotVectCurrentX);
        RotVectCurrentY = findViewById(R.id.RotVectCurrentY);
        RotVectCurrentZ = findViewById(R.id.RotVectCurrentZ);
        RotVectScal = findViewById(R.id.RotVectScalar);

        sCount = findViewById(R.id.StepsCount);
        sDetect = findViewById(R.id.StepsDetect);
        sDetect.setText(String.valueOf(sDetector));
    }
    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(this, mLinearAccel, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(this, mRotationVect, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(this, mStepCount, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(this, mStepDetect, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            setAccelVals(sensorEvent);
            System.arraycopy(sensorEvent.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
            updateOrientationAngles();
        }else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            setLinAccVals(sensorEvent);
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            setGyroVals(sensorEvent);
        }else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            setMagnetoVals(sensorEvent);
            System.arraycopy(sensorEvent.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
            updateOrientationAngles();
        } else if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            setRotVectVals(sensorEvent);
        }
        else if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            sCount.setText(String.valueOf(appSteps));
            //sCount.setText(String.valueOf(sensorEvent.values[0]));
        }
        else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            sDetector++;
            sDetect.setText(String.valueOf(sDetector));
        }
    }

    public void setRotVectVals(SensorEvent sensorEvent)
    {
        RotVectCurrentX.setText(Float.toString(sensorEvent.values[0]));
        RotVectCurrentY.setText(Float.toString(sensorEvent.values[1]));
        RotVectCurrentZ.setText(Float.toString(sensorEvent.values[2]));
        RotVectScal.setText(Float.toString(sensorEvent.values[3]));
    }
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, mMagnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, mOrientationAngles);

        // "mOrientationAngles" now has up-to-date information.
        OrientCurrentX.setText(Float.toString(mOrientationAngles[0]));
        OrientCurrentY.setText(Float.toString(mOrientationAngles[1]));
        OrientCurrentZ.setText(Float.toString(mOrientationAngles[2]));
    }
    public void setMagnetoVals(SensorEvent sensorEvent)
    {
        MagnetoCurrentX.setText(Float.toString(sensorEvent.values[0]));
        MagnetoCurrentY.setText(Float.toString(sensorEvent.values[1]));
        MagnetoCurrentZ.setText(Float.toString(sensorEvent.values[2]));
    }
    public void setGyroVals(SensorEvent sensorEvent)
    {
        GyroCurrentX.setText(Float.toString(sensorEvent.values[0]));
        GyroCurrentY.setText(Float.toString(sensorEvent.values[1]));
        GyroCurrentZ.setText(Float.toString(sensorEvent.values[2]));
    }
    public void setLinAccVals(SensorEvent sensorEvent)
    {
        linAccCurrentX.setText(Float.toString(sensorEvent.values[0]));
        linAccCurrentY.setText(Float.toString(sensorEvent.values[1]));
        linAccCurrentZ.setText(Float.toString(sensorEvent.values[2]));
    }
    public void setAccelVals(SensorEvent sensorEvent)
    {
        // clean current values
        displayCleanValues();
        // display the current x,y,z accelerometer values
        displayCurrentValues();
        // get the change of the x,y,z values of the accelerometer
        adeltaX = Math.abs(alastX -sensorEvent.values[0]);
        adeltaY = Math.abs(alastY - sensorEvent.values[1]);
        adeltaZ = Math.abs(alastZ - sensorEvent.values[2]);

        // if the change is below 2, it is just plain noise
        if (adeltaX < 2)
            adeltaX = 0;
        if (adeltaY < 2)
            adeltaY = 0;
        if (adeltaZ < 2)
            adeltaZ = 0;
        if ((adeltaZ > avibrateThreshold) || (adeltaY > avibrateThreshold) || (adeltaZ > avibrateThreshold)) {
            av.vibrate(50);
        }
    }

    public void displayCleanValues() {
        acurrentX.setText("0.0");
        acurrentY.setText("0.0");
        acurrentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        acurrentX.setText(Float.toString(adeltaX));
        acurrentY.setText(Float.toString(adeltaY));
        acurrentZ.setText(Float.toString(adeltaZ));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
