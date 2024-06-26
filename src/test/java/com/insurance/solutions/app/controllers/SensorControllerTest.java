package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Sensor;
import com.insurance.solutions.app.repositories.SensorRepository;
import com.insurance.solutions.app.services.SensorService;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class SensorControllerTest {

    String urlBase = "/sensors";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorService sensorService;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    private <T> T toClass(MvcResult response, Class<T> type) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getResponse().getContentAsString(), type);
    }

    @Test
    public void createSensor() throws Exception {
        Sensor sensor = new Sensor("name1", "model1");


        // valid
        MvcResult mvcResult = mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(sensor))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        Long id = toClass(mvcResult, Sensor.class).getId();
        sensor.setId(id);

        assertEquals(toJson(sensor), toJson(sensorRepository.findById(id)));


        // invalid
        List<Sensor> sensorsBefore = (List<Sensor>) sensorRepository.findAll();
        int size = sensorsBefore.size();

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(sensor))
                )
                .andExpect(status().isBadRequest());

        List<Sensor> sensorsAfter = (List<Sensor>) sensorRepository.findAll();

        assertEquals(size, sensorsAfter.size());

    }

    @Test
    public void updateSensor() throws Exception {
        Sensor aSensor = new Sensor("name4", "model4");
        Sensor sensorUpdated = new Sensor("name5", "model5");

        Long id = sensorService.createSensor(aSensor).getId();

        Assert.assertEquals(toJson(aSensor), toJson(sensorRepository.findById(id)));

        mockMvc
                .perform(
                        put(urlBase + "/update/" + id)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(sensorUpdated))
                )
                .andExpect(status().isOk());

        sensorUpdated.setId(id);
        Assert.assertEquals(toJson(sensorUpdated), toJson(sensorRepository.findById(id)));

        long mockID = 1000L;

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> sensorService.updateSensor(mockID, sensorUpdated));
        Assert.assertEquals("Sensor not found.", exception.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/update/" + mockID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(sensorUpdated))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteValidSensor() throws Exception {
        Sensor sensor = new Sensor("name2", "model2");
        Sensor newSensor = sensorService.createSensor(sensor);

        long idToDelete = newSensor.getId();

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + idToDelete)
                )
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteInvalidSensor() throws Exception {

        List<Sensor> before = sensorService.getAllSensors();

        long idToDelete = 57775786867867878L;

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + idToDelete)
                )
                .andExpect(status().isNotFound());

        List<Sensor> after = sensorService.getAllSensors();

        assertEquals("Size should be the same", before.size(), after.size());
    }

    @Test
    void getAllSensors() throws Exception {

        List<Sensor> all = sensorService.getAllSensors();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/get-all")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(all)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        Assert.assertEquals("Size should be the same", all.size(), list.size());

    }

    @Test
    void getEmptyAllSensors() throws Exception {

        sensorRepository.deleteAll();

        List<Sensor> all = sensorService.getAllSensors();

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

        Assert.assertEquals("Size should be the same", all.size(), list.size());

    }
}
