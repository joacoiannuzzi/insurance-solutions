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
import java.util.List;

import static com.insurance.solutions.app.models.enums.VehicleCategory.CAR;
import static com.insurance.solutions.app.utils.TestUtil.createRandomDrivingProfile;
import static com.insurance.solutions.app.utils.TestUtil.createRandomVehicle;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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


    @Test
    public void createDrivingProfile() throws Exception {
        final var vehicle = createRandomVehicle();
        final var drivingProfile = createRandomDrivingProfile();

        final var vehicleId = vehicleService.createVehicle(vehicle).getId();


        // valid
        MvcResult mvcResult = mockMvc
                .perform(
                        post(urlBase + "/create/" + vehicleId)
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(drivingProfile))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        final var id = toClass(mvcResult, DrivingProfile.class).getId();
        drivingProfile.setId(id);

        assertEquals(toJson(drivingProfile), toJson(drivingProfileService.findById(id)));


        // invalid

        final var vehicleMockId = 32476577577834L;

        final var exception = assertThrows(ResourceNotFoundException.class,
                () -> drivingProfileService.createDrivingProfile(drivingProfile, vehicleMockId));
        assertEquals("Vehicle not found.", exception.getMessage());

        final var size = drivingProfileService.findAll().size();

        mockMvc
                .perform(
                        post(urlBase + "/create/" + vehicleMockId)
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(drivingProfile))
                )
                .andExpect(status().isNotFound());

        assertEquals(size, drivingProfileService.findAll().size());

    }

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

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> drivingProfileService.findById(mockID));
        assertEquals("Driving profile not found.", exception.getMessage());

        mockMvc
                .perform(get(urlBase + "/" + mockID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateDrivingProfile() throws Exception {
        final var vehicle = createRandomVehicle();
        final var savedVehicle = vehicleService.createVehicle(vehicle);

        final var drivingProfile = createRandomDrivingProfile();

        final var drivingProfileUpdated = createRandomDrivingProfile();

        final var id = drivingProfileService.createDrivingProfile(drivingProfile, savedVehicle.getId()).getId();

        assertEquals(toJson(drivingProfile), toJson(drivingProfileService.findById(id)));

        mockMvc
                .perform(
                        put(urlBase + "/update/" + id)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(drivingProfileUpdated))
                )
                .andExpect(status().isOk());

        drivingProfileUpdated.setId(id);
        assertEquals(toJson(drivingProfileUpdated), toJson(drivingProfileService.findById(id)));

        long mockID = 1000L;

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> drivingProfileService.updateDrivingProfile(mockID, drivingProfileUpdated));
        assertEquals("Driving profile not found.", exception.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/update/" + mockID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(drivingProfileUpdated))
                )
                .andExpect(status().isNotFound());
    }


}