package com.insurance.solutions.app.services;


import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle createVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate()))
            throw new BadRequestException("A vehicle with license plate: " + vehicle.getLicensePlate() + " already exists.");

        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> findAll() {
        return (List<Vehicle>) vehicleRepository.findAll();
    }

    public List<Vehicle> getAllVehiclesWithoutClient() {
        return vehicleRepository.findAllByClientNull();
    }

    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
    }
}
