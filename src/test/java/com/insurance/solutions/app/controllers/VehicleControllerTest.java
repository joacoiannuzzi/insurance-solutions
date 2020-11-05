package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.*;
import com.insurance.solutions.app.models.enums.VehicleCategory;
import com.insurance.solutions.app.repositories.ClientRepository;
import com.insurance.solutions.app.repositories.VehicleRepository;
import com.insurance.solutions.app.services.ClientService;
import com.insurance.solutions.app.services.DrivingProfileService;
import com.insurance.solutions.app.services.MonitoringSystemService;
import com.insurance.solutions.app.services.VehicleService;
import com.insurance.solutions.app.utils.FunctionUtils;
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
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.insurance.solutions.app.models.enums.VehicleCategory.*;
import static com.insurance.solutions.app.utils.VehicleUtils.makeVehicle;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
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

    @Autowired
    private MonitoringSystemService monitoringSystemService;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    private <T> T toClass(MvcResult response, Class<T> type) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getResponse().getContentAsString(), type);
    }

    @Test
    void createValidVehicle() throws Exception {
        vehicleRepository.deleteAll();

        Vehicle vehicle = new Vehicle("5667867678", CAR,
                "ford", "model");

        MvcResult response = mockMvc
                .perform(
                        post(urlBase + "/create")
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

        Vehicle vehicle = new Vehicle("77826348628468", CAR,
                "ford", "model");

        vehicleService.createVehicle(vehicle);

        int size = vehicleService.findAll().size();

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(vehicle))
                )
                .andExpect(status().isBadRequest())
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

        vehicleService.deleteAll();

        final var all = vehicleService.findAll();

        final var emptyList = Collections.emptyList();

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
                "juanperez@mail.com");
        Client savedClient = clientService.createClient(client);

        Vehicle vehicle = new Vehicle("86899789", CAR,
                "ford", "model");
        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        clientService.addVehicle(savedVehicle.getId(), savedClient.getId());

        Client client2 = new Client("3345", "Jorge", "Perez", "3453453",
                "jorgeperez@mail.com");
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
                "juanperez@mail.com");
        Client savedClient = clientService.createClient(client);

        Vehicle vehicle = new Vehicle("86899789", CAR,
                "ford", "model");
        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        clientService.addVehicle(savedVehicle.getId(), savedClient.getId());

        Client client2 = new Client("3345", "Jorge", "Perez", "3453453",
                "jorgeperez@mail.com");
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
                .andExpect(content().string(toJson(makeVehicle(savedVehicle, true))))
                .andReturn();

        assertEquals(toJson(makeVehicle(savedVehicle, true)), response.getResponse().getContentAsString());

        long mockID = 100L;

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> vehicleService.findById(mockID));
        assertEquals("Vehicle not found.", exception.getMessage());

        mockMvc
                .perform(get(urlBase + "/" + mockID))
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
        DrivingProfile savedDrivingProfile1 = drivingProfileService.createDrivingProfile(drivingProfile1, vehicleId);
        DrivingProfile savedDrivingProfile2 = drivingProfileService.createDrivingProfile(drivingProfile2, vehicleId);
        DrivingProfile savedDrivingProfile3 = drivingProfileService.createDrivingProfile(drivingProfile3, vehicleId);

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
        Vehicle aVehicle = new Vehicle("2", VehicleCategory.CAR,
                "brand2", "model2");
        Vehicle vehicleUpdated = new Vehicle("3", VehicleCategory.CAR,
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
        Vehicle vehicle = new Vehicle("1", VehicleCategory.CAR,
                "brand1", "model1");

        long vehicleId = vehicleService.createVehicle(vehicle).getId();


        Assert.assertEquals(toJson(vehicle), toJson(vehicleService.findById(vehicleId)));

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + vehicleId)
                )
                .andExpect(status().isNoContent());

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

    @Test
    void setMonitoringSystemToVehicle() throws Exception {
        final Vehicle vehicle = new Vehicle("345345345", CAR, "brand", "model");
        final MonitoringSystem monitoringSystem = new MonitoringSystem("name_34524234", "monitoringCompany_4598458345");

        Long vehicleId = vehicleService.createVehicle(vehicle).getId();
        final MonitoringSystem savedMonitoringSystem = monitoringSystemService.createMonitoringSystem(monitoringSystem);
        savedMonitoringSystem.setIsAssigned(true);

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleId + "/set-monitoring-system/" + savedMonitoringSystem.getId())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(savedMonitoringSystem)));

        // add existing monitoring system to non existing vehicle
        long vehicleMockId = 1000L;

        Exception exception1 = assertThrows(ResourceNotFoundException.class, () -> vehicleService.setMonitoringSystem(vehicleMockId, savedMonitoringSystem.getId()));
        assertEquals("Vehicle not found.", exception1.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleMockId + "/set-monitoring-system/" + savedMonitoringSystem.getId())
                )
                .andExpect(status().isNotFound());

        // add non existing monitoring system to existing vehicle
        long monitoringSystemMockId = 1000L;

        Exception exception2 = assertThrows(ResourceNotFoundException.class, () -> vehicleService.setMonitoringSystem(vehicleId, monitoringSystemMockId));
        assertEquals("Monitoring system not found.", exception2.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleId + "/set-monitoring-system/" + monitoringSystemMockId)
                )
                .andExpect(status().isNotFound());

        // add not existing monitoring system to not existing vehicle
        Exception exception3 = assertThrows(ResourceNotFoundException.class, () -> vehicleService.setMonitoringSystem(vehicleMockId, monitoringSystemMockId));
        assertEquals("Vehicle not found.", exception3.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/" + vehicleMockId + "/set-monitoring-system/" + monitoringSystemMockId)
                )
                .andExpect(status().isNotFound());
    }


    @Test
    void removeMonitoringSystemToVehicle() throws Exception {
        final var vehicle = new Vehicle("81v45 13v84", CAR,
                "c1p4 r1y2 8", "c12rt 378 ctv8");
        final var monitoringSystem = new MonitoringSystem("1bc478t178ct07", "1c9b4y1b98981c  ce");

        final var vehicleId = vehicleService.createVehicle(vehicle).getId();
        final var monitoringSystemId = monitoringSystemService.createMonitoringSystem(monitoringSystem).getId();
        final var savedMonitoringSystem = vehicleService.setMonitoringSystem(vehicleId, monitoringSystemId);

        mockMvc
                .perform(
                        delete(urlBase + "/" + vehicleId + "/remove-monitoring-system")
                )
                .andExpect(status().isNoContent());

        assertNull(vehicleService.findById(vehicleId).getMonitoringSystem());


        // remove monitoring system from non existing vehicle
        long vehicleMockId = 1000L;

        final var exception1 = assertThrows(ResourceNotFoundException.class,
                () -> vehicleService.removeMonitoringSystem(vehicleMockId)
        );

        assertEquals("Vehicle not found.", exception1.getMessage());

        mockMvc
                .perform(
                        delete(urlBase + "/" + vehicleMockId + "/remove-monitoring-system/")
                )
                .andExpect(status().isNotFound());

        // remove non existing monitoring system from existing vehicle

        Exception exception2 = assertThrows(BadRequestException.class,
                () -> vehicleService.removeMonitoringSystem(vehicleId)
        );
        assertEquals("Vehicle does not have a monitoring system.", exception2.getMessage());

        mockMvc
                .perform(
                        delete(urlBase + "/" + vehicleId + "/remove-monitoring-system/")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    void getAllVehiclesWithoutMonitoringSystem() throws Exception {

        vehicleService.deleteAll();


        // empty list

        final var emptyList = Collections.emptyList();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/without-monitoring-system")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(emptyList)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        assertEquals("Size should be the same", emptyList.size(), list.size());


        // with 10 vehicles

        final var vehicles = Stream.generate(new Random()::nextInt)
                .limit(10)
                .map(number -> new Vehicle("licensePlate_" + number, CAR, "brand_" + number, "model_" + number))
                .map(vehicle -> vehicleService.createVehicle(vehicle))
                .collect(Collectors.toList());

        final var monitoringSystems = Stream.generate(new Random()::nextInt)
                .limit(20)
                .map(number -> new MonitoringSystem("name_" + number, "monitoringCompany_" + number))
                .map(monitoringSystem -> monitoringSystemService.createMonitoringSystem(monitoringSystem))
                .collect(Collectors.toList());


        FunctionUtils.zip(
                vehicles.stream(),
                monitoringSystems.stream(),
                (vehicle, monitoringSystem) -> vehicleService.setMonitoringSystem(vehicle.getId(), monitoringSystem.getId())
        );

        final var allMonitoringSystemsWithoutVehicle = vehicleService.getAllVehiclesWithoutMonitoringSystem();

        MvcResult mvcResult2 = mockMvc
                .perform(
                        get(urlBase + "/without-monitoring-system")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(allMonitoringSystemsWithoutVehicle)))
                .andReturn();

        List list2 = toClass(mvcResult2, List.class);

        assertEquals("Size should be the same", allMonitoringSystemsWithoutVehicle.size(), list2.size());

    }

}