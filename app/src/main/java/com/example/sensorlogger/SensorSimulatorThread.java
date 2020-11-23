package com.example.sensorlogger;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SensorSimulatorThread extends Thread {

    ArrayList<VirtualSensorEvent> virtualSensorEvents;
    VirtualSensorEventListener targetListener;

    public SensorSimulatorThread(VirtualSensorEventListener targetListener, Sensor sensor, String csvPath) {

        this.targetListener = targetListener;

        this.virtualSensorEvents = new ArrayList<>();

        scanCSVDataToVirtualSensorEvents(sensor, csvPath);

        calculateTimeIntervalBetweenSensorEvents();
    }

    private void scanCSVDataToVirtualSensorEvents(Sensor sensor, String csvPath) {
        try {
            File file = new File(csvPath);
            Scanner scanner = new Scanner(file);

            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                VirtualSensorEvent sensorEvent = new VirtualSensorEvent();

                sensorEvent.sensor = sensor;
                sensorEvent.timeUntilEvent = Long.parseLong(data[0]);
                sensorEvent.values[0] = Float.parseFloat(data[1]);
                sensorEvent.values[1] = Float.parseFloat(data[2]);
                sensorEvent.values[2] = Float.parseFloat(data[3]);
                //sensorEvent.status = data[4];

                virtualSensorEvents.add(sensorEvent);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateTimeIntervalBetweenSensorEvents() {
        Long previousTimeStamp = virtualSensorEvents.get(0).timeUntilEvent;

        for (VirtualSensorEvent virtualSensorEvent : virtualSensorEvents) {
            Long currentTimestamp = virtualSensorEvent.timeUntilEvent;
            virtualSensorEvent.timeUntilEvent = currentTimestamp - previousTimeStamp;
            previousTimeStamp = currentTimestamp;
        }
    }

    public void run() {
        for (VirtualSensorEvent sensorEvent : virtualSensorEvents) {
            try {
                TimeUnit.MILLISECONDS.sleep(sensorEvent.timeUntilEvent);
                targetListener.onVirtualSensorChanged(sensorEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
