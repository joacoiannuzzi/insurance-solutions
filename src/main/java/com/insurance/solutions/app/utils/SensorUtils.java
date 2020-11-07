package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.Sensor;
import com.insurance.solutions.app.resources.SensorResource;

import java.util.ArrayList;
import java.util.List;

import static com.insurance.solutions.app.utils.MonitoringSystemUtils.makeMonitoringSystems;

public class SensorUtils {
    public static List<SensorResource> makeSensors(List<Sensor> sensors, boolean relationship) {
        List<SensorResource> sensorResources = new ArrayList<>();
        for (Sensor sensor : sensors) sensorResources.add(makeSensor(sensor, relationship));
        return sensorResources;
    }

    public static SensorResource makeSensor(Sensor sensor, boolean relationship) {
        if (sensor == null) return null;

        return new SensorResource(
                sensor.getId(),
                sensor.getName(),
                sensor.getModel(),
                relationship ? makeMonitoringSystems(new ArrayList<>(sensor.getMonitoringSystems()), false) : new ArrayList<>()
        );
    }
}
