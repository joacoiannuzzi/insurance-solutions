package com.insurance.solutions.app.repositories;

import com.insurance.solutions.app.models.MonitoringSystem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringSystemRepository extends CrudRepository<MonitoringSystem, Long> {

    boolean existsByNameAndSensorAndMonitoringCompany(String name, String sensor, String monitoringCompany);

    List<MonitoringSystem> findAllByVehicleIsNull();

    List<MonitoringSystem> findAllByIsAssignedIsFalse();

}
