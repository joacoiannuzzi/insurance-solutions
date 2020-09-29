package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.repositories.MonitoringSystemRepository;
import com.insurance.solutions.app.services.MonitoringSystemService;
import com.insurance.solutions.app.services.VehicleService;
import com.insurance.solutions.app.utils.FunctionUtils;
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
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.insurance.solutions.app.models.ENUM_CATEGORY.CAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MonitoringSystemControllerTest {

    String urlBase = "/monitoring-systems";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MonitoringSystemService monitoringSystemService;

    @Autowired
    private MonitoringSystemRepository monitoringSystemRepository;

    @Autowired
    private VehicleService vehicleService;


    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    private <T> T toClass(MvcResult response, Class<T> type) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getResponse().getContentAsString(), type);
    }

    @Test
    public void createMonitoringSystem() throws Exception {
        final var monitoringSystem = new MonitoringSystem("name1", "sensor1", "monitoringCompany1");


        // valid
        MvcResult mvcResult = mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(monitoringSystem))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        final var id = toClass(mvcResult, MonitoringSystem.class).getId();
        monitoringSystem.setId(id);

        assertEquals(toJson(monitoringSystem), toJson(monitoringSystemService.findById(id)));


        // invalid
        final int size = monitoringSystemService.findAll().size();

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(monitoringSystem))
                )
                .andExpect(status().isBadRequest());

        assertEquals(size, monitoringSystemService.findAll().size());

    }

    @Test
    public void getMonitoringSystemById() throws Exception {

        final var savedMonitoringSystem = monitoringSystemService.createMonitoringSystem(
                new MonitoringSystem("name_getById", "sensor__getById", "monitoringCompany__getById")
        );

        // valid

        MvcResult response = mockMvc
                .perform(get(urlBase + "/" + savedMonitoringSystem.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(savedMonitoringSystem)))
                .andReturn();

        // invalid
        long mockID = 10000L;

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> monitoringSystemService.findById(mockID));
        assertEquals("Monitoring system not found.", exception.getMessage());

        mockMvc
                .perform(get(urlBase + "/" + mockID))
                .andExpect(status().isNotFound());
    }

    void getAllMonitoringSystem() throws Exception {

        List<MonitoringSystem> all = monitoringSystemService.getAllMonitoringSystems();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/get-all")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(all)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        assertEquals("Size should be the same", all.size(), list.size());

    }

    @Test
    void getEmptyAllMonitoringSystem() throws Exception {

        monitoringSystemRepository.deleteAll();

        List<MonitoringSystem> all = monitoringSystemService.getAllMonitoringSystems();

        List<Object> emptyList = Collections.emptyList();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/get-all")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(emptyList)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        assertEquals("Size should be the same", all.size(), list.size());

    }

    @Test
    void getAllMonitoringSystemWithoutVehicles() throws Exception {

        monitoringSystemService.deleteAll();

        // empty list

        final var emptyList = Collections.emptyList();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/without-vehicle")
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
                .map(number -> new MonitoringSystem("name_" + number, "sensor_" + number, "monitoringCompany_" + number))
                .map(monitoringSystem -> monitoringSystemService.createMonitoringSystem(monitoringSystem))
                .collect(Collectors.toList());


        FunctionUtils.zip(
                vehicles.stream(),
                monitoringSystems.stream(),
                (vehicle, monitoringSystem) -> vehicleService.setMonitoringSystem(vehicle.getId(), monitoringSystem.getId())
        );

        final var allMonitoringSystemsWithoutVehicle = monitoringSystemService.getAllMonitoringSystemsWithoutVehicle();

        MvcResult mvcResult2 = mockMvc
                .perform(
                        get(urlBase + "/without-vehicle")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(allMonitoringSystemsWithoutVehicle)))
                .andReturn();

        List list2 = toClass(mvcResult2, List.class);

        assertEquals("Size should be the same", allMonitoringSystemsWithoutVehicle.size(), list2.size());

    }

}