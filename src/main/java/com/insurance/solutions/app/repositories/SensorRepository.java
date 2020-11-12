package com.insurance.solutions.app.repositories;

import com.insurance.solutions.app.models.Sensor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends CrudRepository<Sensor, Long> {
    boolean existsByName(String name);

    List<Sensor> findAllByMonitoringSystemsNull();
}
