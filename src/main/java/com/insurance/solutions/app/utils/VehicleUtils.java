package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.resources.VehicleResource;

import java.util.ArrayList;
import java.util.List;

import static com.insurance.solutions.app.utils.ClientUtils.makeClient;
import static com.insurance.solutions.app.utils.DrivingProfileUtils.makeDrivingProfiles;
import static com.insurance.solutions.app.utils.MonitoringSystemUtils.makeMonitoringSystem;

public class VehicleUtils {
    public static List<VehicleResource> makeVehicles(List<Vehicle> vehicles, boolean relationship) {
        List<VehicleResource> vehicleResources = new ArrayList<>();
        for (Vehicle vehicle : vehicles) vehicleResources.add(makeVehicle(vehicle, relationship));
        return vehicleResources;
    }

    public static VehicleResource makeVehicle(Vehicle vehicle, boolean relationship) {
        if (vehicle == null) return null;

        MonitoringSystem monitoringSystem = vehicle.getMonitoringSystem();
        Client client = vehicle.getClient();

        return new VehicleResource(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getCategory(),
                vehicle.getBrand(),
                vehicle.getModel(),
                relationship ? makeDrivingProfiles(new ArrayList<>(vehicle.getDrivingProfiles()), false) : new ArrayList<>(),
                relationship && monitoringSystem != null ? makeMonitoringSystem(monitoringSystem, false) : null,
                relationship && client != null ? makeClient(client, false) : null
        );
    }
}
