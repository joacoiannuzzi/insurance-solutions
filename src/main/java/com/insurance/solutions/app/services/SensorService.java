package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.models.Sensor;
import com.insurance.solutions.app.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    public Sensor createSensor(Sensor sensor) {
        if (sensorRepository.existsByName(sensor.getName()))
            throw new BadRequestException("Sensor with name " + sensor.getName() + " already exists.");
        return sensorRepository.save(sensor);
    }
}
