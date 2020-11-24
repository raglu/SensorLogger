package com.example.sensorsimulator;

import android.hardware.Sensor;

public class VirtualSensorManager {

    //Use it like a normal SensorManager for registering listener for your sensor
    //csvPath points to your logged sensor-data
    //remember to make listener implement VirtualSensorEventListener instead of SensorEventListener for this hack-job to work

    public static void registerListener(VirtualSensorEventListener sensorEventListener, Sensor sensor, String csvPath) {

        SensorSimulatorThread sensorSimulatorThread = new SensorSimulatorThread(sensorEventListener, sensor, csvPath);
        sensorSimulatorThread.start();

    }
}
