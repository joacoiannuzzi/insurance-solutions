package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients")
@CrossOrigin
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        return new ResponseEntity<>(clientService.createClient(client), HttpStatus.CREATED);
    }

    @GetMapping("get/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) {
        return new ResponseEntity<>(clientService.getClientById(clientId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Client>> findAll() {
        return new ResponseEntity<>(clientService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable long id) {
        clientService.deleteClientById(id);
        return new ResponseEntity<>("Client was deleted", HttpStatus.OK);
    }
}
