package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.resources.MonitoringSystemResource;
import com.insurance.solutions.app.services.MonitoringSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.insurance.solutions.app.utils.MonitoringSystemUtils.makeMonitoringSystem;
import static com.insurance.solutions.app.utils.MonitoringSystemUtils.makeMonitoringSystems;

@RestController
@RequestMapping("monitoring-systems")
@CrossOrigin
public class MonitoringSystemController {

    @Autowired
    private MonitoringSystemService monitoringSystemService;


    @PostMapping("create")
    public ResponseEntity<MonitoringSystemResource> createMonitoringSystem(@Valid @RequestBody MonitoringSystem monitoringSystem) {
        return new ResponseEntity<>(
                makeMonitoringSystem(monitoringSystemService.createMonitoringSystem(monitoringSystem), true),
                HttpStatus.CREATED
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<MonitoringSystemResource> getMonitoringSystemById(@PathVariable Long id) {
        return ResponseEntity.ok(makeMonitoringSystem(monitoringSystemService.findById(id), true));
    }

    @GetMapping("get-all")
    public ResponseEntity<List<MonitoringSystemResource>> getAllMonitoringSystems() {
        return ResponseEntity.ok(makeMonitoringSystems(monitoringSystemService.getAllMonitoringSystems(), true));
    }

    @DeleteMapping("/delete/{monitoringSystemId}")
    public ResponseEntity<?> deleteMonitoringSystemId(@PathVariable Long monitoringSystemId) {
        monitoringSystemService.deleteMonitoringSystemId(monitoringSystemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("without-vehicle")
    public ResponseEntity<List<MonitoringSystemResource>> getAllMonitoringSystemsWithoutVehicle() {
        return ResponseEntity.ok(
                makeMonitoringSystems(monitoringSystemService.getAllMonitoringSystemsWithoutVehicle(), true)
        );
    }

    @PutMapping("/update/{monitoringSystemId}")
    public ResponseEntity<MonitoringSystemResource> updateMonitoringSystem(@PathVariable Long monitoringSystemId, @RequestBody MonitoringSystem monitoringSystem) {
        return new ResponseEntity<>(
                makeMonitoringSystem(monitoringSystemService.updateMonitoringSystem(monitoringSystemId, monitoringSystem), true),
                HttpStatus.OK
        );
    }
}
