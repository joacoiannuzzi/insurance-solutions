package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@CrossOrigin
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<Client> createBook(@RequestBody Client client) {
        return clientService.createClient(client);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable long id) {
        return clientService.deleteClientById(id);
    }

}
