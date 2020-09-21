package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.ENUM_CATEGORY;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.ClientRepository;
import com.insurance.solutions.app.repositories.VehicleRepository;
import com.insurance.solutions.app.services.ClientService;
import com.insurance.solutions.app.services.VehicleService;
import org.junit.Assert;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VehicleControllerTest {

    String urlBase = "/vehicles";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    private <T> T toClass(MvcResult response, Class<T> type) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getResponse().getContentAsString(), type);
    }

    @Test
    void createValidVehicle() throws Exception {
        clientRepository.deleteAll();
        vehicleRepository.deleteAll();

        Client client = new Client("1", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro");
        Client savedClient = clientService.createClient(client);

        Vehicle vehicle = new Vehicle("86899789", ENUM_CATEGORY.CAR,
                "ford", "model", "drivingProfile", "monitor");


        MvcResult response = mockMvc
                .perform(
                        post(urlBase + "/" + savedClient.getId())
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(vehicle))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        Long id = objectMapper.readValue(response.getResponse().getContentAsString(), Vehicle.class).getId();
        vehicle.setId(id);

        Assert.assertEquals(toJson(vehicle), toJson(vehicleRepository.findById(id)));

    }

    @Test
    void createInvalidVehicleWithExistingLicensePlate() throws Exception {
        clientRepository.deleteAll();
        vehicleRepository.deleteAll();

        Client client = new Client("1", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro");

        Client client2 = new Client("345", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro");

        Client savedClient = clientService.createClient(client);
        Client savedClient2 = clientService.createClient(client2);

        Vehicle vehicle = new Vehicle("86899789", ENUM_CATEGORY.CAR,
                "ford", "model", "drivingProfile", "monitor");

        vehicleService.createVehicle(vehicle);

        int size = vehicleService.findAll().size();

        mockMvc
                .perform(
                        post(urlBase + "/" + savedClient2.getId())
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(vehicle))
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals(size, vehicleService.findAll().size());

    }

    @Test
    void createInvalidVehicleWithInvalidUserId() throws Exception {
        vehicleRepository.deleteAll();

        Vehicle vehicle = new Vehicle("86899789", ENUM_CATEGORY.CAR,
                "ford", "model", "drivingProfile", "monitor");

        int size = vehicleService.findAll().size();

        long invalidUserId = 234234234L;

        mockMvc
                .perform(
                        post(urlBase + "/" + invalidUserId)
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(vehicle))
                )
                .andExpect(status().isNotFound())
                .andReturn();

        Assert.assertEquals(size, vehicleService.findAll().size());

    }

    @Test
    void getAllVehicles() throws Exception {

        List<Vehicle> all = vehicleService.findAll();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(all)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        assertEquals("Size should be the same", all.size(), list.size());

    }

    @Test
    void getEmptyAllVehicles() throws Exception {

        vehicleRepository.deleteAll();

        List<Vehicle> all = vehicleService.findAll();

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

        assertEquals("Size should be the same", all.size(), list.size());

    }

    @Test
    void getAllVehiclesWithoutClients() throws Exception {

        vehicleRepository.deleteAll();
        clientRepository.deleteAll();

        Client client = new Client("1", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro");
        Client savedClient = clientService.createClient(client);

        Vehicle vehicle = new Vehicle("86899789", ENUM_CATEGORY.CAR,
                "ford", "model", "drivingProfile", "monitor");
        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        clientService.addVehicle(savedVehicle.getId(), savedClient.getId());

        Client client2 = new Client("3345", "Jorge", "Perez", "3453453",
                "jorgeperez@mail.com", "SEcure");
        Client savedClient2 = clientService.createClient(client2);

        Vehicle vehicle2 = new Vehicle("34634334", ENUM_CATEGORY.MOTORCYCLE,
                "ford2", "model2", "drivingProfile2", "monitor2");
        Vehicle savedVehicle2 = vehicleService.createVehicle(vehicle2);

        List<Vehicle> allVehiclesWithoutClient = vehicleService.getAllVehiclesWithoutClient();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/clientless")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(allVehiclesWithoutClient)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        assertEquals("Size should be the same", allVehiclesWithoutClient.size(), list.size());

    }

    @Test
    void getEmptyAllVehiclesWithoutClients() throws Exception {

        vehicleRepository.deleteAll();
        clientRepository.deleteAll();

        Client client = new Client("1", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro");
        Client savedClient = clientService.createClient(client);

        Vehicle vehicle = new Vehicle("86899789", ENUM_CATEGORY.CAR,
                "ford", "model", "drivingProfile", "monitor");
        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        clientService.addVehicle(savedVehicle.getId(), savedClient.getId());

        Client client2 = new Client("3345", "Jorge", "Perez", "3453453",
                "jorgeperez@mail.com", "SEcure");
        Client savedClient2 = clientService.createClient(client2);

        Vehicle vehicle2 = new Vehicle("34634334", ENUM_CATEGORY.MOTORCYCLE,
                "ford2", "model2", "drivingProfile2", "monitor2");
        Vehicle savedVehicle2 = vehicleService.createVehicle(vehicle2);

        clientService.addVehicle(savedVehicle2.getId(), savedClient2.getId());

        List<Vehicle> allVehiclesWithoutClient = vehicleService.getAllVehiclesWithoutClient();

        List<Object> emptyList = Collections.emptyList();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/clientless")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(emptyList)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        assertEquals("Size should be the same", allVehiclesWithoutClient.size(), list.size());

    }

    @Test
    void getVehicleById() throws Exception {

        Vehicle vehicle = new Vehicle("48237", ENUM_CATEGORY.TRUCK,
                "ford", "model", "drivingProfile", "monitor");

        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        MvcResult response = mockMvc
                .perform(get(urlBase + "/" + savedVehicle.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(toJson(savedVehicle)))
                .andReturn();

        assertEquals(toJson(savedVehicle), response.getResponse().getContentAsString());

        long mockID = 100L;

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> vehicleService.findById(mockID));
        assertEquals("Vehicle not found.", exception.getMessage());

        mockMvc
                .perform(get(urlBase + "/" + mockID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteExistingVehicle() throws Exception {
        Vehicle vehicle = new Vehicle("1", ENUM_CATEGORY.CAR,
                "brand1", "model1", "drivingProfile1", "monitor1");

        long vehicleId = vehicleService.createVehicle(vehicle).getId();


        Assert.assertEquals(toJson(vehicle), toJson(vehicleService.findById(vehicleId)));

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + vehicleId)
                )
                .andExpect(status().isOk());

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> vehicleService.findById(vehicleId));
        Assert.assertEquals("Vehicle not found.", exception.getMessage());

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + vehicleId)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNotExistingVehicle() throws Exception {

        List<Vehicle> before = vehicleService.findAll();

        long idToDelete = 57775786867867878L;

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + idToDelete)
                )
                .andExpect(status().isNotFound());

        List<Vehicle> after = vehicleService.findAll();

        assertEquals("Size should be the same", before.size(), after.size());
    }

}