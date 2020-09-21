package com.insurance.solutions.app.repositories;

import com.insurance.solutions.app.models.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

    boolean existsByLicensePlate(String licensePlate);

    List<Vehicle> findAllByClientIsNull();

    List<Vehicle> findAllByClientNull();
}
