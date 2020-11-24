package com.example.sensorlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.example.sensorsimulator.VirtualSensorEvent;
import com.example.sensorsimulator.VirtualSensorEventListener;
import com.example.sensorsimulator.VirtualSensorManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements VirtualSensorEventListener {

    boolean isSensorLoggingInProgress;
    int accelerometerCounter, linearAccelerationCounter, gyroscopeCounter;

    Switch accelerometerSwitch, linearAccelerationSwitch, gyroscopeSwitch;
    EditText saveAsEditText;
    Button toggleSensorLoggingButton;
    TextView statusTextView;

    SensorManager sensorManager;

    Sensor accelerometerSensor, linearAccelerationSensor, gyroscopeSensor;

    BufferedWriter accelerometerWriter, linearAccelerationWriter, gyroscopeWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometerSwitch = findViewById(R.id.accelerometerSwitch);
        linearAccelerationSwitch = findViewById(R.id.linearAccelerationSwitch);
        gyroscopeSwitch = findViewById(R.id.gyroscopeSwitch);
        saveAsEditText = findViewById(R.id.saveAsEditText);
        toggleSensorLoggingButton = findViewById(R.id.toggleSensorLoggingButton);
        statusTextView = findViewById(R.id.statusTextView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        accelerometerCounter = 0;
        linearAccelerationCounter = 0;
        gyroscopeCounter = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    public void togglesSensorLogging(View view) throws IOException {
        if (isSensorLoggingInProgress)
            stopSensorLogging();
        else
            startSensorLogging();
    }

    private void startSensorLogging() throws IOException {

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

        String saveAsDirectoryName = this.getApplicationContext().getFilesDir() + "/" + saveAsEditText.getText();
        String saveAsDate = sdf.format(date);
        saveAsDirectoryName += "_" + saveAsDate;

        File dir = new File(saveAsDirectoryName);
        dir.mkdirs();


        if (accelerometerSwitch.isChecked()) {
            //sensorManager.registerListener(MainActivity.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            File accelerometerCSV = new File(saveAsDirectoryName, "accelerometer_" + saveAsDate + ".csv");
            accelerometerCSV.createNewFile();
            accelerometerWriter = new BufferedWriter(new FileWriter(accelerometerCSV));
            accelerometerWriter.write("#timestamp,x-value,y-value,z-value");
            VirtualSensorManager.registerListener(MainActivity.this, accelerometerSensor, "/data/user/0/com.example.sensorlogger/files/testfile_2020-11-22-03:02:29/OnePlus6_Rasmus2_Accelerometer.csv");
        }
        if (linearAccelerationSwitch.isChecked()) {
            // sensorManager.registerListener(MainActivity.this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
            File linearAccelerationCSV = new File(saveAsDirectoryName, "linearAcceleration_" + saveAsDate + ".csv");
            linearAccelerationCSV.createNewFile();
            linearAccelerationWriter = new BufferedWriter(new FileWriter(linearAccelerationCSV));
            linearAccelerationWriter.write("#timestamp,x-value,y-value,z-value");
            VirtualSensorManager.registerListener(MainActivity.this, linearAccelerationSensor, "/data/user/0/com.example.sensorlogger/files/testfile_2020-11-22-03:02:29/OnePlus6_Rasmus2_LinearAcc.csv");
        }
        if (gyroscopeSwitch.isChecked()) {
            // sensorManager.registerListener(MainActivity.this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
            File gyroscopeCSV = new File(saveAsDirectoryName, "gyroscope_" + saveAsDate + ".csv");
            gyroscopeCSV.createNewFile();
            gyroscopeWriter = new BufferedWriter(new FileWriter(gyroscopeCSV));
            gyroscopeWriter.write("#timestamp,x-value,y-value,z-value");
            VirtualSensorManager.registerListener(MainActivity.this, gyroscopeSensor, "/data/user/0/com.example.sensorlogger/files/testfile_2020-11-22-03:02:29/OnePlus6_Rasmus2_Gyroscope.csv");
        }

        isSensorLoggingInProgress = true;
        toggleSensorLoggingButton.setText("Stop Sensor Logging");
    }

    private void stopSensorLogging() throws IOException {

        sensorManager.unregisterListener(MainActivity.this);

        accelerometerCounter = 0;
        linearAccelerationCounter = 0;
        gyroscopeCounter = 0;

        accelerometerWriter.close();
        linearAccelerationWriter.close();
        gyroscopeWriter.close();

        isSensorLoggingInProgress = false;
        toggleSensorLoggingButton.setText("Start Sensor Logging");
    }

    @Override
    public void onVirtualSensorChanged(VirtualSensorEvent event) {
        Sensor sensor = event.sensor;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            try {
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    accelerometerWriter.append("\n" + Calendar.getInstance().getTimeInMillis() + "," + event.values[0] + "," + event.values[1] + "," + event.values[2]);
                    accelerometerCounter++;
                } else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                    linearAccelerationWriter.append("\n" + Calendar.getInstance().getTimeInMillis() + "," + event.values[0] + "," + event.values[1] + "," + event.values[2]);
                    linearAccelerationCounter++;
                } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    gyroscopeWriter.append("\n" + Calendar.getInstance().getTimeInMillis() + "," + event.values[0] + "," + event.values[1] + "," + event.values[2]);
                    gyroscopeCounter++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        statusTextView.setText(String.format("Accelerometer instances: %d\nLinear acceleration instances: %d\nGyroscope instances: %d", accelerometerCounter, linearAccelerationCounter, gyroscopeCounter));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}