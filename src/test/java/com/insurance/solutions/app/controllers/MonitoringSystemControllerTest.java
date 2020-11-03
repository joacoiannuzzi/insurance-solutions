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
import com.insurance.solutions.app.utils.TestUtil;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.insurance.solutions.app.models.enums.VehicleCategory.CAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
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

    @Test
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

        monitoringSystemService.deleteAll();

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

        assertEquals("Size should be the same", emptyList.size(), list.size());

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

        final var vehicles = Stream.generate(TestUtil::createRandomVehicle)
                .limit(10)
                .map(vehicleService::createVehicle)
                .collect(Collectors.toList());

        final var monitoringSystems = Stream.generate(TestUtil::createRandomMonitoringSystem)
                .limit(20)
                .map(monitoringSystemService::createMonitoringSystem)
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

    @Test
    public void deleteExistingMonitoringSystem() throws Exception {
        MonitoringSystem monitoringSystem = new MonitoringSystem("name2", "sensor2", "monitoringCompany2");

        long monitoringSystemId = monitoringSystemService.createMonitoringSystem(monitoringSystem).getId();


        Assert.assertEquals(toJson(monitoringSystem), toJson(monitoringSystemService.findById(monitoringSystemId)));

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + monitoringSystemId)
                )
                .andExpect(status().isNoContent());

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> monitoringSystemService.findById(monitoringSystemId));
        Assert.assertEquals("Monitoring system not found.", exception.getMessage());

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + monitoringSystemId)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNotExistingMonitoringSystem() throws Exception {

        List<MonitoringSystem> before = monitoringSystemService.findAll();

        long idToDelete = 57775786867867878L;

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + idToDelete)
                )
                .andExpect(status().isNotFound());

        List<MonitoringSystem> after = monitoringSystemService.findAll();

        assertEquals("Size should be the same", before.size(), after.size());
    }

    @Test
    public void updateMonitoringSystem() throws Exception {

        // existing monitoring system without vehicle
        final var monitoringSystem = new MonitoringSystem("374589378945", "70349qqnavkas", "awynr89vaw89");
        final var monitoringSystemUpdated = new MonitoringSystem("nafjh983v53", "kjwhbviy348by", "v3b406104bv");

        final var monitoringSystemId = monitoringSystemService.createMonitoringSystem(monitoringSystem).getId();

        assertEquals(toJson(monitoringSystem), toJson(monitoringSystemService.findById(monitoringSystemId)));

        mockMvc
                .perform(
                        put(urlBase + "/update/" + monitoringSystemId)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(monitoringSystemUpdated))
                )
                .andExpect(status().isOk());

        monitoringSystemUpdated.setId(monitoringSystemId);
        assertEquals(toJson(monitoringSystemUpdated), toJson(monitoringSystemService.findById(monitoringSystemId)));

        // existing monitoring system with vehicle
        final var monitoringSystem2 = new MonitoringSystem("345v8n7138957 b", "v3y49y 1v3914v", "51v451 t875b1");
        final var monitoringSystem2Id = monitoringSystemService.createMonitoringSystem(monitoringSystem2).getId();

        final var vehicle = new Vehicle("8b6892486b2398", CAR, "hb78v78ty42876vt6", "fqv 4t9y2894t98 q389q3");
        final var vehicleId = vehicleService.createVehicle(vehicle).getId();

        final var monitoringSystemWithVehicle = vehicleService.setMonitoringSystem(vehicleId, monitoringSystem2Id);

        assertEquals(toJson(monitoringSystemWithVehicle), toJson(monitoringSystemService.findById(monitoringSystem2Id)));

        final var monitoringSystem2Updated = new MonitoringSystem("b52378v3ytvy3", "pyvb7yv33v", "13vtb783tbv7838tv");


        mockMvc
                .perform(
                        put(urlBase + "/update/" + monitoringSystem2Id)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(monitoringSystem2Updated))
                )
                .andExpect(status().isOk());

        monitoringSystem2Updated.setId(monitoringSystem2Id);
        monitoringSystem2Updated.setIsAssigned(true);

        assertEquals(toJson(monitoringSystem2Updated), toJson(monitoringSystemService.findById(monitoringSystem2Id)));

        final var id = vehicleService.findById(vehicleId).getMonitoringSystem().getId();

        assertEquals(monitoringSystem2Id, id);

        // non existing monitoring system
        final var mockID = 1000L;

        final var exception = assertThrows(ResourceNotFoundException.class, () -> monitoringSystemService.updateMonitoringSystem(mockID, monitoringSystemUpdated));
        assertEquals("Monitoring system not found.", exception.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/update/" + mockID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(monitoringSystemUpdated))
                )
                .andExpect(status().isNotFound());
    }

}
