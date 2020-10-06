package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.services.DrivingProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("driving-profiles")
@CrossOrigin
public class DrivingProfileController {

    @Autowired
    private DrivingProfileService drivingProfileService;

    @PostMapping("/create/{vehicleId}")
    public ResponseEntity<DrivingProfile> createDrivingProfile(@PathVariable Long vehicleId, @Valid @RequestBody DrivingProfile drivingProfile) {
        return ResponseEntity.ok(drivingProfileService.createDrivingProfile(drivingProfile, vehicleId));
    }

    @DeleteMapping("/delete/{drivingProfileId}")
    public ResponseEntity<?> deleteDrivingProfile(@PathVariable Long drivingProfileId) {
        drivingProfileService.deleteDrivingProfile(drivingProfileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}")
    public ResponseEntity<DrivingProfile> getDrivingProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(drivingProfileService.findById(id));
    }
}
