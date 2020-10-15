package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.ClientRepository;
import com.insurance.solutions.app.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public Client createClient(Client client) {
        if (clientRepository.existsByDniAndInsuranceCompany(client.getDni(), client.getInsuranceCompany()))
            throw new BadRequestException("El cliente con DNI: " + client.getDni() + " y perteneciente a la compania " +
                    client.getInsuranceCompany() + " ya existe.");

        return clientRepository.save(client);
    }

    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));
    }

    public List<Client> findAll() {
        return (List<Client>) clientRepository.findAll();
    }

    public void deleteClientById(Long id) {
        Client client = getClientById(id);
        for (Vehicle vehicle : client.getVehicles()) {
            vehicle.setClient(null);
            client.removeVehicle(vehicle);
            vehicleRepository.save(vehicle);
        }
        clientRepository.save(client);
        clientRepository.deleteById(id);
    }

    public Client updateClient(Long clientId, Client client) {
        Client oldClient = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));
        Client newClient = new Client(client.getDni(), client.getFirstName(), client.getLastName(), client.getPhoneNumber(), client.getMail());

        newClient.setId(oldClient.getId());
        return clientRepository.save(newClient);
    }

    public List<Vehicle> getClientVehicles(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));
        return new ArrayList<>(client.getVehicles());
    }

    public Vehicle addVehicle(Long vehicleId, Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));

        vehicle.setClient(client);
        client.addVehicle(vehicle);

        clientRepository.save(client);
        return vehicle;
    }

    public Vehicle deleteVehicle(Long vehicleId, Long clientId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
        if (vehicle.getClient() == null || !vehicle.getClient().getId().equals(clientId)) throw new BadRequestException("Vehicle does not belong to client.");

        vehicle.setClient(null);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehiclesWithoutClient() {
        return vehicleRepository.findAllByClientIsNull();
    }
}
