package com.insurance.solutions.app.services;


import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.DrivingProfileRepository;
import com.insurance.solutions.app.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DrivingProfileRepository drivingProfileRepository;

    @Autowired
    private MonitoringSystemService monitoringSystemService;

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

    public Vehicle updateVehicle(Long vehicleId, Vehicle vehicle) {
        Vehicle oldVehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
        Vehicle newVehicle = new Vehicle(vehicle.getLicensePlate(), vehicle.getCategory(), vehicle.getBrand(), vehicle.getModel());

        newVehicle.setId(oldVehicle.getId());
        return vehicleRepository.save(newVehicle);
    }

    public void deleteVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
        vehicleRepository.delete(vehicle);
    }

    public List<DrivingProfile> getDrivingProfilesOfVehicle(Long vehicleId) {
        var vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));
        return new ArrayList<>(vehicle.getDrivingProfiles());
    }


    public DrivingProfile addDrivingProfile(Long vehicleId, Long drivingProfileId) {
        Vehicle vehicle = findById(vehicleId);
        DrivingProfile drivingProfile = drivingProfileRepository.findById(drivingProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Driving profile not found."));

        drivingProfile.setVehicle(vehicle);
        vehicle.addDrivingProfile(drivingProfile);

        vehicleRepository.save(vehicle);
        return drivingProfile;
    }

    public void deleteDrivingProfile(Long vehicleId, Long drivingProfileId) {
        DrivingProfile drivingProfile = drivingProfileRepository.findById(drivingProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Driving profile not found."));

        Vehicle vehicle = drivingProfile.getVehicle();

        if (vehicle == null || !drivingProfile.getVehicle().getId().equals(vehicleId))
            throw new BadRequestException("Driving profile does not belong to vehicle.");

        vehicle.removeDrivingProfile(drivingProfile);
        vehicleRepository.save(vehicle);
    }

    public void deleteAll() {
        List<Vehicle> vehicles = findAll();
        for (Vehicle vehicle : vehicles) {
            for (DrivingProfile drivingProfile : vehicle.getDrivingProfiles())
                vehicle.removeDrivingProfile(drivingProfile);
            vehicle.setMonitoringSystem(null);
            vehicle.setClient(null);
            vehicleRepository.save(vehicle);
        }
        vehicleRepository.deleteAll();
    }

    public MonitoringSystem setMonitoringSystem(Long vehicleId, Long monitoringSystemId) {
        final var vehicle = findById(vehicleId);
        final var monitoringSystem = monitoringSystemService.findById(monitoringSystemId);

        monitoringSystem.setVehicle(vehicle);
        monitoringSystem.setIsAssigned(true);

        vehicle.setMonitoringSystem(monitoringSystem);

        vehicleRepository.save(vehicle);
        return monitoringSystem;
    }

    public List<Vehicle> getAllVehiclesWithoutMonitoringSystem() {
        return vehicleRepository.findAllByMonitoringSystemIsNull();
    }

    public void removeMonitoringSystem(Long vehicleId) {
        final var vehicle = findById(vehicleId);
        final var monitoringSystem = vehicle.getMonitoringSystem();

        if (monitoringSystem == null)
            throw new BadRequestException("Vehicle does not have a monitoring system.");

        monitoringSystem.setIsAssigned(false);
        monitoringSystem.setVehicle(null);

        vehicle.setMonitoringSystem(null);
        vehicleRepository.save(vehicle);

    }
}
