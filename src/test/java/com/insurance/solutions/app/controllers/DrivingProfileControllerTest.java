package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.services.DrivingProfileService;
import com.insurance.solutions.app.services.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.insurance.solutions.app.models.ENUM_CATEGORY.CAR;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Autowired
    private VehicleService vehicleService;

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
    void deleteDrivingProfile() throws Exception {

        Vehicle vehicle = new Vehicle("99999", CAR, "brand999", "model999");


        DrivingProfile drivingProfile = createRandomDrivingProfile();

        long drivingProfileMockID = 1000L;

        long vehicleId = vehicleService.createVehicle(vehicle).getId();

        DrivingProfile savedDrivingProfile = drivingProfileService.createDrivingProfile(drivingProfile, vehicleId);

        List<DrivingProfile> beforeVehicleDrivingProfiles = vehicleService.getDrivingProfilesOfVehicle(vehicleId);

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + savedDrivingProfile.getId())
                )
                .andExpect(status().isNoContent());

        List<DrivingProfile> afterVehicleDrivingProfiles = vehicleService.getDrivingProfilesOfVehicle(vehicleId);

        assertNotEquals("Size should not be the same", beforeVehicleDrivingProfiles.size(), afterVehicleDrivingProfiles.size());

        // Delete not existing drivingProfile

        Exception exception2 = assertThrows(ResourceNotFoundException.class, () -> drivingProfileService.deleteDrivingProfile(drivingProfileMockID));
        assertEquals("Driving profile not found.", exception2.getMessage());

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + drivingProfileMockID)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDrivingProfileById() throws Exception {
        Vehicle vehicle = new Vehicle("72634123", CAR, "Test Brand", "Test Model");

        long vehicleId = vehicleService.createVehicle(vehicle).getId();

        final var savedDrivingProfile = drivingProfileService.createDrivingProfile(
                createRandomDrivingProfile(), vehicleId);

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