package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.services.DrivingProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DrivingProfileControllerTest {

    String urlBase = "/driving-profiles";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DrivingProfileService drivingProfileService;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    private <T> T toClass(MvcResult response, Class<T> type) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getResponse().getContentAsString(), type);
    }

    private DrivingProfile createRandomDrivingProfile() {
        final var random = new Random();

        return new DrivingProfile(random.nextDouble(), random.nextDouble(), random.nextDouble(),
                random.nextDouble(), random.nextDouble(), new Date(random.nextInt()), new Date(random.nextInt()));
    }


//    @Test
//    public void createMonitoringSystem() throws Exception {
//        final var monitoringSystem = new MonitoringSystem("name1", "sensor1", "monitoringCompany1");
//
//
//        // valid
//        MvcResult mvcResult = mockMvc
//                .perform(
//                        post(urlBase + "/create")
//                                .contentType(APPLICATION_JSON)
//                                .accept(APPLICATION_JSON)
//                                .content(toJson(monitoringSystem))
//                )
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").isNumber())
//                .andReturn();
//
//        final var id = toClass(mvcResult, MonitoringSystem.class).getId();
//        monitoringSystem.setId(id);
//
//        assertEquals(toJson(monitoringSystem), toJson(monitoringSystemService.findById(id)));
//
//
//        // invalid
//        final int size = monitoringSystemService.findAll().size();
//
//        mockMvc
//                .perform(
//                        post(urlBase + "/create")
//                                .contentType(APPLICATION_JSON)
//                                .accept(APPLICATION_JSON)
//                                .content(toJson(monitoringSystem))
//                )
//                .andExpect(status().isBadRequest());
//
//        assertEquals(size, monitoringSystemService.findAll().size());
//
//    }

    @Test
    public void getDrivingProfileById() throws Exception {

        final var savedDrivingProfile = drivingProfileService.createDrivingProfile(
                createRandomDrivingProfile()
        );

        // valid

        mockMvc
                .perform(get(urlBase + "/" + savedDrivingProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(savedDrivingProfile)));
        // invalid
        long mockID = 10000L;

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> drivingProfileService.findById(mockID));
        assertEquals("Driving profile not found.", exception.getMessage());

        mockMvc
                .perform(get(urlBase + "/" + mockID))
                .andExpect(status().isNotFound());
    }

}