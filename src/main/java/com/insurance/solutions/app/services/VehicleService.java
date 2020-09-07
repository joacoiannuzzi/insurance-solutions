package com.insurance.solutions.app.services;


import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.ClientRepository;
import com.insurance.solutions.app.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Vehicle createVehicle(Long clientId, Vehicle vehicle) {
        if (vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate()))
            throw new BadRequestException("A vehicle with license plate: " + vehicle.getLicensePlate() + " already exists.");

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id: " + clientId + " not found."));

        vehicle.setClient(client);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        client.addVehicle(savedVehicle);
        clientRepository.save(client);
        return savedVehicle;
    }

    public List<Vehicle> findAll() {
        return (List<Vehicle>) vehicleRepository.findAll();
    }
}
