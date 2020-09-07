package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        vehicleService.createVehicle(savedClient.getId(), vehicle);

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

}