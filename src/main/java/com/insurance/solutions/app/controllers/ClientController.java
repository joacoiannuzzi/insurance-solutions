package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.resources.ClientResource;
import com.insurance.solutions.app.resources.VehicleResource;
import com.insurance.solutions.app.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.insurance.solutions.app.utils.ClientUtils.makeClient;
import static com.insurance.solutions.app.utils.ClientUtils.makeClients;
import static com.insurance.solutions.app.utils.VehicleUtils.makeVehicle;
import static com.insurance.solutions.app.utils.VehicleUtils.makeVehicles;

@RestController
@RequestMapping("/clients")
@CrossOrigin
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<ClientResource> createClient(@Valid @RequestBody Client client) {
        return new ResponseEntity<>(makeClient(clientService.createClient(client), true), HttpStatus.CREATED);
    }

    @GetMapping("get/{clientId}")
    public ResponseEntity<ClientResource> getClientById(@PathVariable Long clientId) {
        return new ResponseEntity<>(makeClient(clientService.getClientById(clientId), true), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ClientResource>> findAll() {
        return new ResponseEntity<>(makeClients(clientService.findAll(), true), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{clientId}")
    public ResponseEntity<ClientResource> updateClient(@PathVariable Long clientId, @Valid @RequestBody Client client) {
        return new ResponseEntity<>(makeClient(clientService.updateClient(clientId, client), true), HttpStatus.OK);
    }

    @GetMapping("/vehicles/{clientId}")
    public ResponseEntity<List<VehicleResource>> getClientVehicles(@PathVariable Long clientId) {
        return new ResponseEntity<>(makeVehicles(clientService.getClientVehicles(clientId), true), HttpStatus.OK);
    }

    @PutMapping("/{clientId}/add-vehicle/{vehicleId}")
    public ResponseEntity<VehicleResource> addVehicle(@PathVariable Long vehicleId, @PathVariable Long clientId) {
        return new ResponseEntity<>(makeVehicle(clientService.addVehicle(vehicleId, clientId), true), HttpStatus.OK);
    }

    @PutMapping("/{clientId}/delete-vehicle/{vehicleId}")
    public ResponseEntity<VehicleResource> deleteVehicle(@PathVariable Long vehicleId, @PathVariable Long clientId) {
        return new ResponseEntity<>(makeVehicle(clientService.deleteVehicle(vehicleId, clientId), true), HttpStatus.OK);
    }

    @GetMapping("/vehicles-without-client")
    public ResponseEntity<List<VehicleResource>> getVehiclesWithoutClient() {
        return new ResponseEntity<>(makeVehicles(clientService.getVehiclesWithoutClient(), true), HttpStatus.OK);
    }

}
