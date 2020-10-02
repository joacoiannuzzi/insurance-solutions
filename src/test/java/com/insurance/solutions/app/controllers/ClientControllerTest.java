package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.ENUM_CATEGORY;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.ClientRepository;
import com.insurance.solutions.app.services.ClientService;
import com.insurance.solutions.app.services.VehicleService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientControllerTest {

    String urlBase = "/clients";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private VehicleService vehicleService;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    private <T> T toClass(MvcResult response, Class<T> type) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getResponse().getContentAsString(), type);
    }

    @BeforeEach
    public void resetClients() {
        clientRepository.saveAll(
                List.of(
                        new Client("4564564", "Annie", "Sims", "(006)-902-3913",
                                "annie.sims@example.com", "segur car"),

                        new Client("342342", "Leo", "Lucas", "(304)-520-6733",
                                "leo.lucas@example.com", "osde"),

                        new Client("5252342", "Olivia", "Pena", "(097)-119-2103",
                                "olivia.pena@example.com", "LA CUEVA MOTOS"),

                        new Client("345345343", "Clayton", "Murphy", "(237)-281-4159",
                                "clayton.murphy@example.com", "Seguros Russo / Taller Felix")

                )
        );
    }


    @Test
    void createValidClient() throws Exception {
        Client client = new Client("1", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro");
        client.setId(100L);

        MvcResult response = mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        long id = toClass(response, Client.class).getId();
        client.setId(id);

        assertEquals(toJson(client), toJson(clientService.getClientById(id)));

    }

    @Test
    void createInvalidClient() throws Exception {
        Client client = new Client();
        client.setDni("1");

        int clientQuantity = clientService.findAll().size();

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                )
                .andExpect(status().isBadRequest());

        assertEquals(clientQuantity, clientService.findAll().size());

    }

    @Test
    void createExistingClient() throws Exception {
        Client client = new Client("2", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 2");

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                );

        int clientQuantity = clientService.findAll().size();

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                )
                .andExpect(status().isBadRequest());

        assertEquals(clientQuantity, clientService.findAll().size());

    }

    @Test
    void getClientById() throws Exception {
        Client client = new Client("3", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 3");

        Client newClient = clientService.createClient(client);

        MvcResult response = mockMvc
                .perform(get("/clients/get/" + newClient.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(toJson(client)))
                .andReturn();

        assertEquals(toJson(newClient), response.getResponse().getContentAsString());

        long mockID = 10056747576567L;

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(mockID));
        assertEquals("Client not found.", exception.getMessage());

        mockMvc
                .perform(get("/clients/get/" + mockID))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteValidClient() throws Exception {

        long idToDelete = clientService.findAll().get(0).getId();

        mockMvc
                .perform(
                        delete(urlBase + "/" + idToDelete)
                )
                    .andExpect(status().isNoContent());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(idToDelete));
        assertEquals("Client not found.", exception.getMessage());
    }

    @Test
    void deleteInvalidClient() throws Exception {

        List<Client> before = clientService.findAll();

        long idToDelete = 57775786867867878L;

        mockMvc
                .perform(
                        delete(urlBase + "/" + idToDelete)
                )
                .andExpect(status().isNotFound());

        List<Client> after = clientService.findAll();

        assertEquals("Size should be the same", before.size(), after.size());
    }

    @Test
    void getAllClients() throws Exception {

        List<Client> all = clientService.findAll();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(all)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        Assert.assertEquals("Size should be the same", all.size(), list.size());

    }

    @Test
    void getEmptyAllClients() throws Exception {

        clientRepository.deleteAll();

        List<Client> all = clientService.findAll();

        List<Object> emptyList = Collections.emptyList();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(emptyList)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        Assert.assertEquals("Size should be the same", all.size(), list.size());

    }

    @Test
    public void updateClients() throws Exception {
        Client client = new Client("4", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 4");
        Client updatedClient = new Client("5", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 5");

        Long id = clientService.createClient(client).getId();

        Assert.assertEquals(toJson(client), toJson(clientService.getClientById(id)));

        mockMvc
                .perform(
                        put("/clients/update/" + id)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(updatedClient))
                )
                .andExpect(status().isOk());

        updatedClient.setId(id);
        Assert.assertEquals(toJson(updatedClient), toJson(clientService.getClientById(id)));

        long mockID = 100L;

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> clientService.updateClient(mockID, updatedClient));
        Assert.assertEquals("Client not found.", exception.getMessage());

        mockMvc
                .perform(
                        put("/clients/update/" + mockID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(updatedClient))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void addVehicleToClient() throws Exception {

        Client client = new Client("1000", "name", "last name", "123",
                "mail@mail.com", "company");


        Vehicle vehicle = new Vehicle("1", ENUM_CATEGORY.CAR,
                "brand", "model");

        long clientId = clientService.createClient(client).getId();

        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        mockMvc
                .perform(
                        put(urlBase + "/" + clientId + "/add-vehicle/" + savedVehicle.getId())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(savedVehicle)));

        // add existing vehicle to not existing client
        long clientMockID = 1000L;

        Exception exception1 = assertThrows(ResourceNotFoundException.class, () -> clientService.addVehicle(savedVehicle.getId(), clientMockID));
        assertEquals("Client not found.", exception1.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + clientMockID + "/add-vehicle/" + savedVehicle.getId())
                )
                .andExpect(status().isNotFound());

        // add not existing vehicle to existing client
        long vehicleMockID = 1000L;

        Exception exception2 = assertThrows(ResourceNotFoundException.class, () -> clientService.addVehicle(vehicleMockID, clientId));
        assertEquals("Vehicle not found.", exception2.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + clientId + "/add-vehicle/" + vehicleMockID)
                )
                .andExpect(status().isNotFound());

        // add not existing vehicle to not existing client
        Exception exception3 = assertThrows(ResourceNotFoundException.class, () -> clientService.addVehicle(vehicleMockID, clientMockID));
        assertEquals("Client not found.", exception3.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + clientMockID + "/add-vehicle/" + vehicleMockID)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVehicleFromClient() throws Exception {

        Client client = new Client("11", "name1", "last name 2", "1234",
                "mail2@mail.com", "company2");


        Vehicle vehicle = new Vehicle("2", ENUM_CATEGORY.CAR,
                "brand2", "model2");

        long vehicleMockID = 1000L;

        long clientMockID = 1000L;

        long clientId = clientService.createClient(client).getId();

        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        clientService.addVehicle(savedVehicle.getId(), clientId);

        List<Vehicle> beforeClientVehicles = clientService.getClientVehicles(clientId);

        mockMvc
                .perform(
                        put(urlBase + "/" + clientId + "/delete-vehicle/" + vehicle.getId())
                )
                .andExpect(status().isOk());

        List<Vehicle> afterClientVehicles = clientService.getClientVehicles(clientId);

        Assert.assertNotEquals("Size should not be the same", beforeClientVehicles.size(), afterClientVehicles.size());

        // Delete existing vehicle in not existing client

        Exception exception1 = assertThrows(BadRequestException.class, () -> clientService.deleteVehicle(savedVehicle.getId(), clientMockID));
        assertEquals("Vehicle does not belong to client.", exception1.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + clientMockID + "/delete-vehicle/" + savedVehicle.getId())
                )
                .andExpect(status().isBadRequest());

        // Delete not existing vehicle in existing client

        Exception exception2 = assertThrows(ResourceNotFoundException.class, () -> clientService.deleteVehicle(vehicleMockID, clientId));
        assertEquals("Vehicle not found.", exception2.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + clientId + "/delete-vehicle/" + vehicleMockID)
                )
                .andExpect(status().isNotFound());

        // Delete not existing vehicle in not existing client

        Exception exception3 = assertThrows(ResourceNotFoundException.class, () -> clientService.deleteVehicle(vehicleMockID, clientMockID));
        assertEquals("Vehicle not found.", exception3.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + clientMockID + "/delete-vehicle/" + vehicleMockID)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void getClientVehicles() throws Exception {
        long clientId = clientService.findAll().get(0).getId();
        List<Vehicle> vehicles = clientService.getClientVehicles(clientId);
        long clientMockID = 1000L;

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/vehicles/" + clientId)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        Assert.assertEquals("Size should be the same", vehicles.size(), list.size());

        mockMvc
                .perform(
                        get(urlBase + "/vehicles/" + clientMockID)
                )
                .andExpect(status().isNotFound());
    }

}