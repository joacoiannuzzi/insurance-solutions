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

    public MonitoringSystem createMonitoringSystem(MonitoringSystem monitoringSystem) {
        if (monitoringSystemRepository.existsByNameAndSensorAndMonitoringCompany(monitoringSystem.getName(), monitoringSystem.getSensor(), monitoringSystem.getMonitoringCompany()))
            throw new BadRequestException("Monitoring system already exists.");

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

    public List<MonitoringSystem> getAllMonitoringSystemsWithoutVehicle() {
        return monitoringSystemRepository.findAllByIsAssignedIsFalse();
    }

    public void deleteAll() {
        findAll().forEach(monitoringSystem -> {
            monitoringSystem.setVehicle(null);
            monitoringSystem.setAssigned(false);
            monitoringSystemRepository.save(monitoringSystem);
        });
        monitoringSystemRepository.deleteAll();
    }

    public void deleteMonitoringSystemId(Long monitoringSystemId) {
        monitoringSystemRepository.findById(monitoringSystemId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoring system not found."));

        monitoringSystemRepository.deleteById(monitoringSystemId);
    }

    public MonitoringSystem updateMonitoringSystem(Long monitoringSystemId, MonitoringSystem monitoringSystem) {
        MonitoringSystem oldMonitoringSystem = monitoringSystemRepository.findById(monitoringSystemId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoring system not found."));
        MonitoringSystem newMonitoringSystem = new MonitoringSystem(monitoringSystem.getName(), monitoringSystem.getSensor(),
                monitoringSystem.getMonitoringCompany());
        newMonitoringSystem.setVehicle(oldMonitoringSystem.getVehicle());

        newMonitoringSystem.setId(oldMonitoringSystem.getId());
        return monitoringSystemRepository.save(newMonitoringSystem);
    }
}
