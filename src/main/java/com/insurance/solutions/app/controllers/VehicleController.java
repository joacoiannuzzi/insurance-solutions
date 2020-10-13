package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.resources.DrivingProfileResource;
import com.insurance.solutions.app.resources.MonitoringSystemResource;
import com.insurance.solutions.app.resources.VehicleResource;
import com.insurance.solutions.app.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.insurance.solutions.app.utils.DrivingProfileUtils.makeDrivingProfiles;
import static com.insurance.solutions.app.utils.MonitoringSystemUtils.makeMonitoringSystem;
import static com.insurance.solutions.app.utils.VehicleUtils.makeVehicle;
import static com.insurance.solutions.app.utils.VehicleUtils.makeVehicles;


@RestController
@RequestMapping("/vehicles")
@CrossOrigin
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/create")
    public ResponseEntity<VehicleResource> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        return new ResponseEntity<>(
                makeVehicle(vehicleService.createVehicle(vehicle), true),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<VehicleResource>> getAllVehicles() {
        return ResponseEntity.ok(makeVehicles(vehicleService.findAll(), true));
    }

    @GetMapping("clientless")
    public ResponseEntity<List<VehicleResource>> getAllVehiclesWithoutClients() {
        return ResponseEntity.ok(makeVehicles(vehicleService.getAllVehiclesWithoutClient(), true));
    }

    @GetMapping("{id}")
    public ResponseEntity<VehicleResource> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(makeVehicle(vehicleService.findById(id), true));
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<VehicleResource> updateVehicle(@PathVariable Long vehicleId, @RequestBody Vehicle vehicle) {
        return new ResponseEntity<>(
                makeVehicle(vehicleService.updateVehicle(vehicleId, vehicle), true),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/driving-profile/{vehicleId}")
    public ResponseEntity<List<DrivingProfileResource>> getClientVehicles(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(
                makeDrivingProfiles(vehicleService.getDrivingProfilesOfVehicle(vehicleId), true)
        );
    }

    @PutMapping("/{vehicleId}/set-monitoring-system/{monitoringSystemId}")
    public ResponseEntity<MonitoringSystemResource> addMonitoringSystem(@PathVariable Long vehicleId, @PathVariable Long monitoringSystemId) {
        return ResponseEntity.ok(
                makeMonitoringSystem(vehicleService.setMonitoringSystem(vehicleId, monitoringSystemId), true)
        );
    }

    @DeleteMapping("/{vehicleId}/remove-monitoring-system")
    public ResponseEntity<?> deleteMonitoringSystem(@PathVariable Long vehicleId) {
        vehicleService.removeMonitoringSystem(vehicleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("without-monitoring-system")
    public ResponseEntity<List<VehicleResource>> getAllVehiclesWithoutMonitoringSystem() {
        return ResponseEntity.ok(makeVehicles(vehicleService.getAllVehiclesWithoutMonitoringSystem(), true));
    }
}
