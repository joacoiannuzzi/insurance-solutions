package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.models.MonitoringSystem;
import com.insurance.solutions.app.repositories.MonitoringSystemRepository;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.services.MonitoringSystemService;
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
    public void deleteExistingMonitoringSystem() throws Exception {
        MonitoringSystem monitoringSystem = new MonitoringSystem("name2", "sensor2", "monitoringCompany2");

        long monitoringSystemId = monitoringSystemService.createMonitoringSystem(monitoringSystem).getId();


        Assert.assertEquals(toJson(monitoringSystem), toJson(monitoringSystemService.findById(monitoringSystemId)));

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + monitoringSystemId)
                )
                .andExpect(status().isOk());

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
}