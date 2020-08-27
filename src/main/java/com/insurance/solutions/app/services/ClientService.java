package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;


    public Optional<Client> findById(long id) {
        return clientRepository.findById(id);
    }

    public void deleteAll() {
        clientRepository.deleteAll();
    }

    public Iterable<Client> saveAll(Iterable<Client> clients) {
        return clientRepository.saveAll(clients);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public ResponseEntity<Client> createClient(Client client) {
        if (clientRepository.existsByDniAndInsuranceCompany(client.getDni(), client.getInsuranceCompany()))
            throw new BadRequestException("El cliente con DNI: " + client.getDni() + " y perteneciente a la compania " +
                    client.getInsuranceCompany() + " ya existe.");

        Client savedClient = clientRepository.save(client);
        return ResponseEntity.ok(savedClient);
    }

    public ResponseEntity<?> deleteClientById(long id) {
        return clientRepository.findById(id)
                .map(__ -> {
                    clientRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                })
                .orElseThrow(() -> new BadRequestException("El cliente con id: " + id + " no existe."));
    }

    public ResponseEntity<List<Client>> findAll() {
        List<Client> all = (List<Client>) clientRepository.findAll();
        return ResponseEntity.ok(all);
    }
}
