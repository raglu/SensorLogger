package com.example.sensorsimulator;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

//unfortunately cannot extend SensorEvent
public class VirtualSensorEvent {

    public float[] values;

    public VirtualSensorEvent() {
        this.values = new float[3];
    }

    public Sensor sensor;

    public int accuracy;

    public long timestamp;

    //this attribute is not used SensorEvent
    public String status;
    public Long timeUntilEvent;

}
