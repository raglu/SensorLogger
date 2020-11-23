package com.example.sensorlogger;

import android.hardware.SensorEventListener;

public interface VirtualSensorEventListener extends SensorEventListener {
    public void onVirtualSensorChanged(VirtualSensorEvent event);
}
