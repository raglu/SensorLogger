package com.example.sensorsimulator;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class SimulationDemo extends Activity implements VirtualSensorEventListener {

    public SimulationDemo() {

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //use this method to listen to real the sensor
        //sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //use this method to listen to the logged sensor data from the csv file
        VirtualSensorManager.registerListener(this, accelerometerSensor, "/data/user/0/com.example.sensorlogger/files/testfile_2020-11-22-03:02:29/OnePlus6_Rasmus2_Accelerometer.csv");
    }

    //this method is called when listening to logged sensor data from csv file
    //it works the same as 'onSensorChanged'
    @Override
    public void onVirtualSensorChanged(VirtualSensorEvent event) {
        System.out.print("x-value:" + event.values[0] + "  y-value:" + event.values[1] + "  z-value:" + event.values[2]);

        //'event.status' cannot be used in onSensorChanged however
        System.out.println("  Status:" + event.status);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
