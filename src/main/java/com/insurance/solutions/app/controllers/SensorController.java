package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Sensor;
import com.insurance.solutions.app.resources.SensorResource;
import com.insurance.solutions.app.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.insurance.solutions.app.utils.SensorUtils.makeSensor;

@RestController
@RequestMapping("sensors")
@CrossOrigin
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @PostMapping("/create")
    public ResponseEntity<SensorResource> createSensor(@Valid @RequestBody Sensor sensor) {
        return new ResponseEntity<>(makeSensor(sensorService.createSensor(sensor), true), HttpStatus.CREATED);
    }
}
