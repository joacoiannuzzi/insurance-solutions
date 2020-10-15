package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.resources.DrivingProfileResource;
import com.insurance.solutions.app.services.DrivingProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.insurance.solutions.app.utils.DrivingProfileUtils.makeDrivingProfile;

@RestController
@RequestMapping("driving-profiles")
@CrossOrigin
public class DrivingProfileController {

    @Autowired
    private DrivingProfileService drivingProfileService;

    @PostMapping("/create/{vehicleId}")
    public ResponseEntity<DrivingProfileResource> createDrivingProfile(@PathVariable Long vehicleId, @Valid @RequestBody DrivingProfile drivingProfile) {
        return new ResponseEntity<>(
                makeDrivingProfile(drivingProfileService.createDrivingProfile(drivingProfile, vehicleId), true),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete/{drivingProfileId}")
    public ResponseEntity<?> deleteDrivingProfile(@PathVariable Long drivingProfileId) {
        drivingProfileService.deleteDrivingProfile(drivingProfileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}")
    public ResponseEntity<DrivingProfileResource> getDrivingProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(makeDrivingProfile(drivingProfileService.findById(id), true));
    }

    @PutMapping("update/{drivingProfileId}")
    public ResponseEntity<DrivingProfileResource> updateDrivingProfile(@PathVariable Long drivingProfileId, @RequestBody DrivingProfile drivingProfile) {
        return ResponseEntity.ok(
                makeDrivingProfile(drivingProfileService.updateDrivingProfile(drivingProfileId, drivingProfile), true)
        );
    }


}
