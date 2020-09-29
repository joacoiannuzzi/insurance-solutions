package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @GetMapping("clientless")
    public ResponseEntity<List<Vehicle>> getAllVehiclesWithoutClients() {
        return ResponseEntity.ok(vehicleService.getAllVehiclesWithoutClient());
    }

    @GetMapping("{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long vehicleId, @RequestBody Vehicle vehicle) {
        return new ResponseEntity<>(vehicleService.updateVehicle(vehicleId, vehicle), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return new ResponseEntity<>("Vehicle deleted", HttpStatus.OK);
    }

    @GetMapping("/driving-profile/{vehicleId}")
    public ResponseEntity<List<DrivingProfile>> getClientVehicles(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleService.getDrivingProfilesOfVehicle(vehicleId));
    }

    @PutMapping("/{vehicleId}/add-driving-profile/{drivingProfileId}")
    public ResponseEntity<DrivingProfile> addVehicle(@PathVariable Long vehicleId, @PathVariable Long drivingProfileId) {
        return ResponseEntity.ok(vehicleService.addDrivingProfile(vehicleId, drivingProfileId));
    }

    @DeleteMapping("/{vehicleId}/delete-driving-profile/{drivingProfileId}")
    public ResponseEntity<String> deleteDrivingProfile(@PathVariable Long vehicleId, @PathVariable Long drivingProfileId) {
        vehicleService.deleteDrivingProfile(vehicleId, drivingProfileId);
        return new ResponseEntity<>("Driving profile deleted", HttpStatus.OK);
    }
}
