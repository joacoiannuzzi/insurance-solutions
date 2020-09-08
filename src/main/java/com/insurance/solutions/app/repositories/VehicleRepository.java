package com.insurance.solutions.app.repositories;

import com.insurance.solutions.app.models.Vehicle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

    boolean existsByLicensePlate(String licensePlate);

    List<Vehicle> findAllByClientNull();
}
