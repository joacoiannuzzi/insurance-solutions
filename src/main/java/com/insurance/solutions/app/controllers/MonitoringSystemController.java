package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.services.MonitoringSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("monitoring-systems")
@CrossOrigin
public class MonitoringSystemController {

    @Autowired
    private MonitoringSystemService monitoringSystemService;


    @PostMapping("create")
    public ResponseEntity<MonitoringSystem> createMonitoringSystem(@Valid @RequestBody MonitoringSystem monitoringSystem) {
        return new ResponseEntity<>(monitoringSystemService.createMonitoringSystem(monitoringSystem), HttpStatus.CREATED);
    }
}