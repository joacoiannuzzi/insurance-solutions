package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.MonitoringSystemRepository;
import com.insurance.solutions.app.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoringSystemService {

    @Autowired
    private MonitoringSystemRepository monitoringSystemRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public MonitoringSystem createMonitoringSystem(MonitoringSystem monitoringSystem) {
        validateMonitoringSystem(monitoringSystem);

        return monitoringSystemRepository.save(monitoringSystem);
    }

    public List<MonitoringSystem> findAll() {
        return (List<MonitoringSystem>) monitoringSystemRepository.findAll();
    }

    public MonitoringSystem findById(Long id) {
        return monitoringSystemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoring system not found."));
    }

    public List<MonitoringSystem> getAllMonitoringSystems() {
        return (List<MonitoringSystem>) monitoringSystemRepository.findAll();
    }

    public void deleteMonitoringSystemId(Long monitoringSystemId) {
        MonitoringSystem monitoringSystem = monitoringSystemRepository.findById(monitoringSystemId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoring system not found."));

        if (monitoringSystem.getIsAssigned()) {
            Vehicle vehicle = monitoringSystem.getVehicle();
            if (vehicle != null) {
                vehicle.setMonitoringSystem(null);
                monitoringSystem.setVehicle(null);
                monitoringSystem.setIsAssigned(false);
                vehicleRepository.save(vehicle);
            }
        }
        monitoringSystemRepository.save(monitoringSystem);
        monitoringSystemRepository.deleteById(monitoringSystemId);
    }

    public MonitoringSystem updateMonitoringSystem(Long monitoringSystemId, MonitoringSystem monitoringSystem) {
        MonitoringSystem oldMonitoringSystem = monitoringSystemRepository.findById(monitoringSystemId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoring system not found."));

        MonitoringSystem newMonitoringSystem = new MonitoringSystem(monitoringSystem.getName(),
                monitoringSystem.getMonitoringCompany());
        newMonitoringSystem.setVehicle(oldMonitoringSystem.getVehicle());
        newMonitoringSystem.setSensor(oldMonitoringSystem.getSensor());
        newMonitoringSystem.setIsAssigned(oldMonitoringSystem.getIsAssigned());

        if (!monitoringSystem.getName().equals(oldMonitoringSystem.getName())) validateMonitoringSystem(newMonitoringSystem);

        newMonitoringSystem.setId(oldMonitoringSystem.getId());
        return monitoringSystemRepository.save(newMonitoringSystem);
    }

    public List<MonitoringSystem> getAllMonitoringSystemsWithoutVehicle() {
        return monitoringSystemRepository.findAllByVehicleIsNull();
    }

    public void deleteAll() {
        findAll().forEach(monitoringSystem -> {
            monitoringSystem.setVehicle(null);
            monitoringSystem.setIsAssigned(false);
            monitoringSystemRepository.save(monitoringSystem);
        });
        monitoringSystemRepository.deleteAll();
    }

    private void validateMonitoringSystem(MonitoringSystem monitoringSystem) {
        if (monitoringSystemRepository.existsByName(monitoringSystem.getName()))
            throw new BadRequestException("Monitoring system with name: " + monitoringSystem.getName() + " already exists.");
    }
}
