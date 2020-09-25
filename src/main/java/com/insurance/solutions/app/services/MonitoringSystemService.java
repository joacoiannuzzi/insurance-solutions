package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.repositories.MonitoringSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitoringSystemService {

    @Autowired
    private MonitoringSystemRepository monitoringSystemRepository;

    public MonitoringSystem createMonitoringSystem(MonitoringSystem monitoringSystem) {
        if (monitoringSystemRepository.existsByNameAndSensorAndMonitoringCompany(monitoringSystem.getName(), monitoringSystem.getSensor(), monitoringSystem.getMonitoringCompany()))
            throw new BadRequestException("Monitoring system already exists.");

        return monitoringSystemRepository.save(monitoringSystem);
    }
}
