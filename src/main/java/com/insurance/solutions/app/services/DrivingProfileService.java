package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.DrivingProfileRepository;
import com.insurance.solutions.app.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrivingProfileService {

    @Autowired
    private DrivingProfileRepository drivingProfileRepository;

    @Autowired
    private VehicleRepository vehicleRepository;


    public DrivingProfile createDrivingProfile(DrivingProfile drivingProfile, Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));

        drivingProfile.setVehicle(vehicle);
        return drivingProfileRepository.save(drivingProfile);
    }

    public void deleteDrivingProfile(Long drivingProfileId) {
        DrivingProfile drivingProfile = drivingProfileRepository.findById(drivingProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Driving profile not found."));

        Vehicle vehicle = drivingProfile.getVehicle();

        vehicle.removeDrivingProfile(drivingProfile);
        vehicleRepository.save(vehicle);
    }


    public DrivingProfile findById(Long id) {
        return drivingProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driving profile not found."));
    }
}
