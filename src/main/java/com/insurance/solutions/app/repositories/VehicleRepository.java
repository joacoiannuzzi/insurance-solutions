package com.insurance.solutions.app.repositories;

import com.insurance.solutions.app.models.Vehicle;
import org.springframework.data.repository.CrudRepository;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

    boolean existsByLicensePlate(String licensePlate);
}
