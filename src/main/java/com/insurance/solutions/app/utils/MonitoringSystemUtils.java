package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.models.Sensor;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.resources.MonitoringSystemResource;

import java.util.ArrayList;
import java.util.List;

import static com.insurance.solutions.app.utils.SensorUtils.makeSensor;
import static com.insurance.solutions.app.utils.VehicleUtils.makeVehicle;

public class MonitoringSystemUtils {
    public static List<MonitoringSystemResource> makeMonitoringSystems(List<MonitoringSystem> monitoringSystems, boolean relationship) {
        List<MonitoringSystemResource> monitoringSystemResources = new ArrayList<>();
        for (MonitoringSystem monitoringSystem : monitoringSystems)
            monitoringSystemResources.add(makeMonitoringSystem(monitoringSystem, relationship));
        return monitoringSystemResources;
    }

    public static MonitoringSystemResource makeMonitoringSystem(MonitoringSystem monitoringSystem, boolean relationship) {
        if (monitoringSystem == null) return null;

        Vehicle vehicle = monitoringSystem.getVehicle();
        Sensor sensor = monitoringSystem.getSensor();
        return new MonitoringSystemResource(
                monitoringSystem.getId(),
                monitoringSystem.getName(),
                monitoringSystem.getMonitoringCompany(),
                monitoringSystem.getIsAssigned(),
                relationship && vehicle != null ? makeVehicle(vehicle, false) : null,
                relationship && sensor != null ? makeSensor(sensor, false) : null
        );
    }
}
