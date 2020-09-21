package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.models.ENUM_CATEGORY;
import com.insurance.solutions.app.repositories.ClientRepository;
import com.insurance.solutions.app.repositories.VehicleRepository;
import com.insurance.solutions.app.services.ClientService;
import com.insurance.solutions.app.services.DrivingProfileService;
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
import java.util.Date;
import java.util.List;

import static com.insurance.solutions.app.models.ENUM_CATEGORY.*;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private DrivingProfileService drivingProfileService;

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

        Vehicle vehicle = new Vehicle("86899789", CAR,
                "ford", "model");


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

        assertEquals(toJson(vehicle), toJson(vehicleRepository.findById(id)));

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

        Vehicle vehicle = new Vehicle("86899789", CAR,
                "ford", "model");

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

        assertEquals(size, vehicleService.findAll().size());

    }

    @Test
    void createInvalidVehicleWithInvalidUserId() throws Exception {
        vehicleRepository.deleteAll();

        Vehicle vehicle = new Vehicle("86899789", CAR,
                "ford", "model");

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

        assertEquals(size, vehicleService.findAll().size());

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

        Vehicle vehicle = new Vehicle("86899789", CAR,
                "ford", "model");
        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        clientService.addVehicle(savedVehicle.getId(), savedClient.getId());

        Client client2 = new Client("3345", "Jorge", "Perez", "3453453",
                "jorgeperez@mail.com", "SEcure");
        Client savedClient2 = clientService.createClient(client2);

        Vehicle vehicle2 = new Vehicle("34634334", MOTORCYCLE,
                "ford2", "model2");
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

        Vehicle vehicle = new Vehicle("86899789", CAR,
                "ford", "model");
        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        clientService.addVehicle(savedVehicle.getId(), savedClient.getId());

        Client client2 = new Client("3345", "Jorge", "Perez", "3453453",
                "jorgeperez@mail.com", "SEcure");
        Client savedClient2 = clientService.createClient(client2);

        Vehicle vehicle2 = new Vehicle("34634334", MOTORCYCLE,
                "ford2", "model2");
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

        Vehicle vehicle = new Vehicle("48237", TRUCK,
                "ford", "model");

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
    void addDrivingProfileToVehicle() throws Exception {
        Vehicle vehicle = new Vehicle("72634", CAR, "brand", "model");

        DrivingProfile drivingProfile = new DrivingProfile(130, 150, 50,
                3423, 2423, new Date(), new Date());

        Long vehicleId = vehicleService.createVehicle(vehicle).getId();
        DrivingProfile savedDrivingProfile = drivingProfileService.createDrivingProfile(drivingProfile);

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleId + "/add-driving-profile/" + savedDrivingProfile.getId())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(savedDrivingProfile)));

        // add existing driving profile to not existing vehicle
        long vehicleMockId = 1000L;

        Exception exception1 = assertThrows(ResourceNotFoundException.class, () -> vehicleService.addDrivingProfile(vehicleMockId, savedDrivingProfile.getId()));
        assertEquals("Vehicle not found.", exception1.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleMockId + "/add-driving-profile/" + savedDrivingProfile.getId())
                )
                .andExpect(status().isNotFound());

        // add not existing driving profile to existing vehicle
        long drivingProfileMockID = 1000L;

        Exception exception2 = assertThrows(ResourceNotFoundException.class, () -> vehicleService.addDrivingProfile(vehicleId, drivingProfileMockID));
        assertEquals("Driving profile not found.", exception2.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleId + "/add-driving-profile/" + drivingProfileMockID)
                )
                .andExpect(status().isNotFound());

        // add not existing driving profile to not existing vehicle
        Exception exception3 = assertThrows(ResourceNotFoundException.class, () -> vehicleService.addDrivingProfile(vehicleMockId, drivingProfileMockID));
        assertEquals("Vehicle not found.", exception3.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleMockId + "/add-vehicle/" + drivingProfileMockID)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDrivingProfileFromClient() throws Exception {

        Vehicle vehicle = new Vehicle("72634", CAR, "brand", "model");

        DrivingProfile drivingProfile = new DrivingProfile(130, 150, 50,
                3423, 2423, new Date(), new Date());


        long drivingProfileMockID = 1000L;

        long vehicleMockID = 1000L;

        long vehicleId = vehicleService.createVehicle(vehicle).getId();

        var savedDrivingProfile = drivingProfileService.createDrivingProfile(drivingProfile);

        vehicleService.addDrivingProfile(vehicleId, savedDrivingProfile.getId());

        List<DrivingProfile> beforeVehicleDrivingProfiles = vehicleService.getDrivingProfilesOfVehicle(vehicleId);

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleId + "/delete-driving-profile/" + drivingProfile.getId())
                )
                .andExpect(status().isOk());

        List<DrivingProfile> afterVehicleDrivingProfiles = vehicleService.getDrivingProfilesOfVehicle(vehicleId);

        assertNotEquals("Size should not be the same", beforeVehicleDrivingProfiles.size(), afterVehicleDrivingProfiles.size());

        // Delete existing drivingProfile in not existing vehicle

        Exception exception1 = assertThrows(BadRequestException.class, () -> vehicleService.deleteDrivingProfile(vehicleMockID, savedDrivingProfile.getId()));
        assertEquals("Driving profile does not belong to vehicle.", exception1.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleMockID + "/delete-driving-profile/" + savedDrivingProfile.getId())
                )
                .andExpect(status().isBadRequest());

        // Delete not existing drivingProfile in existing vehicle

        Exception exception2 = assertThrows(ResourceNotFoundException.class, () -> vehicleService.deleteDrivingProfile(vehicleId, drivingProfileMockID));
        assertEquals("Driving profile not found.", exception2.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleId + "/delete-driving-profile/" + drivingProfileMockID)
                )
                .andExpect(status().isNotFound());

        // Delete not existing drivingProfile in not existing vehicle

        Exception exception3 = assertThrows(ResourceNotFoundException.class, () -> vehicleService.deleteDrivingProfile(vehicleMockID, drivingProfileMockID));
        assertEquals("Driving profile not found.", exception3.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleMockID + "/delete-driving-profile/" + drivingProfileMockID)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void getDrivingProfilesOfVehicle() throws Exception {

        Vehicle vehicle = new Vehicle("7578578", CAR, "brand", "model");

        DrivingProfile drivingProfile1 = new DrivingProfile(130, 534, 324,
                234, 2423, new Date(), new Date());

        DrivingProfile drivingProfile2 = new DrivingProfile(234, 263, 342,
                234, 2423, new Date(), new Date());

        DrivingProfile drivingProfile3 = new DrivingProfile(234, 346, 50,
                456, 345, new Date(), new Date());

        Long vehicleId = vehicleService.createVehicle(vehicle).getId();
        DrivingProfile savedDrivingProfile1 = drivingProfileService.createDrivingProfile(drivingProfile1);
        DrivingProfile savedDrivingProfile2 = drivingProfileService.createDrivingProfile(drivingProfile2);
        DrivingProfile savedDrivingProfile3 = drivingProfileService.createDrivingProfile(drivingProfile3);

        vehicleService.addDrivingProfile(vehicleId, savedDrivingProfile1.getId());
        vehicleService.addDrivingProfile(vehicleId, savedDrivingProfile2.getId());
        vehicleService.addDrivingProfile(vehicleId, savedDrivingProfile3.getId());

        List<DrivingProfile> drivingProfiles = List.of(savedDrivingProfile1, savedDrivingProfile2, savedDrivingProfile3);


        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/driving-profile/" + vehicleId)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(drivingProfiles)))
                .andReturn();

        List drivingProfilesResponse = toClass(mvcResult, List.class);

        assertEquals("Size should be the same", drivingProfiles.size(), drivingProfilesResponse.size());

        // empty

        Vehicle vehicle2 = new Vehicle("4573845", CAR, "brand", "model");
        Long vehicle2Id = vehicleService.createVehicle(vehicle2).getId();

        List<Object> emptyList = Collections.emptyList();

        MvcResult mvcResult2 = mockMvc
                .perform(
                        get(urlBase + "/driving-profile/" + vehicle2Id)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(emptyList)))
                .andReturn();

        List drivingProfilesResponse2 = toClass(mvcResult2, List.class);

        assertEquals("Size should be the same", emptyList.size(), drivingProfilesResponse2.size());

        long vehicleMockID = 1000L;

        mockMvc
                .perform(
                        get(urlBase + "/driving-profile/" + vehicleMockID)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateVehicle() throws Exception {
        Vehicle aVehicle = new Vehicle("2", ENUM_CATEGORY.CAR,
                "brand2", "model2");
        Vehicle vehicleUpdated = new Vehicle("3", ENUM_CATEGORY.CAR,
                "brand3", "model3");

        Long id = vehicleService.createVehicle(aVehicle).getId();

        Assert.assertEquals(toJson(aVehicle), toJson(vehicleService.findById(id)));

        mockMvc
                .perform(
                        put(urlBase + "/update/" + id)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(vehicleUpdated))
                )
                .andExpect(status().isOk());

        vehicleUpdated.setId(id);
        Assert.assertEquals(toJson(vehicleUpdated), toJson(vehicleService.findById(id)));

        long mockID = 1000L;

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> vehicleService.updateVehicle(mockID, vehicleUpdated));
        Assert.assertEquals("Vehicle not found.", exception.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/update/" + mockID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(vehicleUpdated))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteExistingVehicle() throws Exception {
        Vehicle vehicle = new Vehicle("1", ENUM_CATEGORY.CAR,
                "brand1", "model1");

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