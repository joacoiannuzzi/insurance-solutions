package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

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
        getClientById(id);
        clientRepository.deleteById(id);
    }

    public Client updateClient(Long clientId, Client client) {
        Client oldClient = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));
        Client newClient = new Client(client.getDni(), client.getFirstName(), client.getLastName(), client.getPhoneNumber(),
                client.getMail(), client.getInsuranceCompany());

        newClient.setId(oldClient.getId());
        return clientRepository.save(newClient);
    }
}
