package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/vehicles")
@CrossOrigin
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("{clientId}")
    public ResponseEntity<Vehicle> createVehicle(@PathVariable Long clientId, @Valid @RequestBody Vehicle vehicle) {
        return new ResponseEntity<>(vehicleService.createVehicle(clientId, vehicle), HttpStatus.CREATED);
    }
}
