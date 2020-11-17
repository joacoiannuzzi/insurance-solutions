package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.DrivingProfileRepository;
import com.insurance.solutions.app.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class DrivingProfileService {

    @Autowired
    private DrivingProfileRepository drivingProfileRepository;

    @Autowired
    private VehicleRepository vehicleRepository;


    public DrivingProfile createDrivingProfile(DrivingProfile drivingProfile, Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));

        validateDates(drivingProfile);

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

    public List<DrivingProfile> findAll() {
        return (List<DrivingProfile>) drivingProfileRepository.findAll();
    }

    public DrivingProfile updateDrivingProfile(Long drivingProfileId, DrivingProfile drivingProfile) {
        validateDates(drivingProfile);

        final var oldDrivingProfile = findById(drivingProfileId);
        final var newDrivingProfile = new DrivingProfile(drivingProfile.getAvgSpeed(),
                drivingProfile.getMaxSpeed(),
                drivingProfile.getMinSpeed(),
                drivingProfile.getTotalDrivingTime(),
                drivingProfile.getAvgDailyDrivingTime(),
                drivingProfile.getStartDate(),
                drivingProfile.getFinishDate()
        );

        newDrivingProfile.setVehicle(oldDrivingProfile.getVehicle());
        newDrivingProfile.setId(oldDrivingProfile.getId());
        return drivingProfileRepository.save(newDrivingProfile);
    }

    private void validateDates(DrivingProfile drivingProfile) {
        Date minimumDate = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
        if (drivingProfile.getStartDate().before(minimumDate) || drivingProfile.getFinishDate().before(minimumDate))
            throw new BadRequestException("La fecha debe ser posterior al año 2000.");
    }
}
