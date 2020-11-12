package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Sensor;
import com.insurance.solutions.app.resources.SensorResource;
import com.insurance.solutions.app.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.insurance.solutions.app.utils.SensorUtils.makeSensor;
import static com.insurance.solutions.app.utils.SensorUtils.makeSensors;

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

    @GetMapping("/get-all")
    public ResponseEntity<List<SensorResource>> getAllSensors() {
        return ResponseEntity.ok(makeSensors(sensorService.getAllSensors(), true));
    }

    @PutMapping("/update/{sensorId}")
    public ResponseEntity<SensorResource> updateSensor(@PathVariable Long sensorId, @Valid @RequestBody Sensor sensor) {
        return new ResponseEntity<>(makeSensor(sensorService.updateSensor(sensorId, sensor), true), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{sensorId}")
    public ResponseEntity<?> deleteSensor(@PathVariable Long sensorId) {
        sensorService.deleteSensor(sensorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/unassigned/get-all")
    public ResponseEntity<List<SensorResource>> getAllUnassignedSensors() {
        return ResponseEntity.ok(makeSensors(sensorService.getAllUnassignedSensors(), true));
    }
}
