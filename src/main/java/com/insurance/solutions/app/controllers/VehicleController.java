package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.resources.ClientResource;
import com.insurance.solutions.app.resources.DrivingProfileResource;
import com.insurance.solutions.app.resources.MonitoringSystemResource;
import com.insurance.solutions.app.resources.VehicleResource;
import com.insurance.solutions.app.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/vehicles")
@CrossOrigin
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/create")
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        return new ResponseEntity<>(vehicleService.createVehicle(vehicle), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResource>> getAllVehicles() {
        return ResponseEntity.ok(makeVehicles(vehicleService.findAll()));
    }

    @GetMapping("clientless")
    public ResponseEntity<List<VehicleResource>> getAllVehiclesWithoutClients() {
        return ResponseEntity.ok(makeVehicles(vehicleService.getAllVehiclesWithoutClient()));
    }

    @GetMapping("{id}")
    public ResponseEntity<VehicleResource> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(makeVehicle(vehicleService.findById(id)));
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long vehicleId, @RequestBody Vehicle vehicle) {
        return new ResponseEntity<>(vehicleService.updateVehicle(vehicleId, vehicle), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/driving-profile/{vehicleId}")
    public ResponseEntity<List<DrivingProfile>> getClientVehicles(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleService.getDrivingProfilesOfVehicle(vehicleId));
    }

    private List<VehicleResource> makeVehicles(List<Vehicle> vehicles) {
        List<VehicleResource> vehicleResources = new ArrayList<>();
        for (Vehicle vehicle : vehicles) vehicleResources.add(makeVehicle(vehicle));
        return vehicleResources;
    }

    private VehicleResource makeVehicle(Vehicle vehicle) {
        List<DrivingProfileResource> drivingProfileResources = makeDrivingProfiles(new ArrayList<>(vehicle.getDrivingProfiles()));
        MonitoringSystemResource monitoringSystemResource =
                vehicle.getMonitoringSystem() != null ? makeMonitoringSystem(vehicle.getMonitoringSystem()) : null;
        ClientResource clientResource = vehicle.getClient() != null ? makeClient(vehicle.getClient()) : null;

        return new VehicleResource(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getCategory(),
                vehicle.getBrand(),
                vehicle.getModel(),
                drivingProfileResources,
                monitoringSystemResource,
                clientResource
        );
    }

    private List<DrivingProfileResource> makeDrivingProfiles(List<DrivingProfile> drivingProfiles) {
        List<DrivingProfileResource> drivingProfileResources = new ArrayList<>();
        for (DrivingProfile drivingProfile : drivingProfiles)
            drivingProfileResources.add(makeDrivingProfile(drivingProfile));
        return drivingProfileResources;
    }

    private DrivingProfileResource makeDrivingProfile(DrivingProfile drivingProfile) {
        return new DrivingProfileResource(
                drivingProfile.getId(),
                drivingProfile.getAvgSpeed(),
                drivingProfile.getMaxSpeed(),
                drivingProfile.getMinSpeed(),
                drivingProfile.getTotalDrivingTime(),
                drivingProfile.getAvgDailyDrivingTime(),
                drivingProfile.getStartDate(),
                drivingProfile.getFinishDate()
        );
    }

    private MonitoringSystemResource makeMonitoringSystem(MonitoringSystem monitoringSystem) {
        return new MonitoringSystemResource(
                monitoringSystem.getId(),
                monitoringSystem.getName(),
                monitoringSystem.getSensor(),
                monitoringSystem.getMonitoringCompany(),
                monitoringSystem.getIsAssigned()
        );
    }

    private ClientResource makeClient(Client client) {
        return new ClientResource(
                client.getId(),
                client.getDni(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.getMail(),
                client.getInsuranceCompany()
        );

    }

    @PutMapping("/{vehicleId}/set-monitoring-system/{monitoringSystemId}")
    public ResponseEntity<MonitoringSystem> addMonitoringSystem(@PathVariable Long vehicleId, @PathVariable Long monitoringSystemId) {
        return ResponseEntity.ok(vehicleService.setMonitoringSystem(vehicleId, monitoringSystemId));
    }

    @DeleteMapping("/{vehicleId}/remove-monitoring-system")
    public ResponseEntity<?> deleteMonitoringSystem(@PathVariable Long vehicleId) {
        vehicleService.removeMonitoringSystem(vehicleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("without-monitoring-system")
    public ResponseEntity<List<Vehicle>> getAllVehiclesWithoutMonitoringSystem() {
        return ResponseEntity.ok(vehicleService.getAllVehiclesWithoutMonitoringSystem());
    }
}
