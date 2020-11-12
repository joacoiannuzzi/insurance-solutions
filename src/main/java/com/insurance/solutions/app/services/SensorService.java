package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.models.Sensor;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.*;
import com.insurance.solutions.app.repositories.MonitoringSystemRepository;
import com.insurance.solutions.app.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MonitoringSystemRepository monitoringSystemRepository;

    public Sensor createSensor(Sensor sensor) {
        if (sensorRepository.existsByName(sensor.getName()))
            throw new BadRequestException("Sensor with name " + sensor.getName() + " already exists.");
        return sensorRepository.save(sensor);
    }

    public List<Sensor> getAllSensors() {
        return (List<Sensor>) sensorRepository.findAll();
    }

    public void deleteSensor(Long sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new ResourceNotFoundException("Sensor not found."));
        for (MonitoringSystem monitoringSystem : sensor.getMonitoringSystems()) {
            monitoringSystem.setSensor(null);
            monitoringSystemRepository.save(monitoringSystem);
        }
        sensor.setMonitoringSystems(new HashSet<>());

        sensorRepository.save(sensor);
        sensorRepository.delete(sensor);
    }

    public List<Sensor> getAllUnassignedSensors() {
        return sensorRepository.findAllByMonitoringSystemsNull();
    }
}
