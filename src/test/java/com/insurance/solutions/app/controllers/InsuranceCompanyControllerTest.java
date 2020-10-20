package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.InsuranceCompany;
import com.insurance.solutions.app.repositories.InsuranceCompanyRepository;
import com.insurance.solutions.app.services.ClientService;
import com.insurance.solutions.app.services.InsuranceCompanyService;
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

import static com.insurance.solutions.app.utils.TestUtil.createRandomInsuranceCompany;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class InsuranceCompanyControllerTest {

    String urlBase = "/insurance-companies";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InsuranceCompanyRepository insuranceCompanyRepository;

    @Autowired
    private InsuranceCompanyService insuranceCompanyService;

    @Autowired
    private ClientService clientService;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    private <T> T toClass(MvcResult response, Class<T> type) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getResponse().getContentAsString(), type);
    }


    @Test
    void createValidInsuranceCompany() throws Exception {
        insuranceCompanyRepository.deleteAll();

        InsuranceCompany insuranceCompany = new InsuranceCompany("company1");

        MvcResult response = mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(insuranceCompany))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        Long id = objectMapper.readValue(response.getResponse().getContentAsString(), InsuranceCompany.class).getId();
        insuranceCompany.setId(id);

        assertEquals(toJson(insuranceCompany), toJson(insuranceCompanyRepository.findById(id)));

    }

    @Test
    void createInvalidInsuranceCompanyWithExistingName() throws Exception {

        InsuranceCompany insuranceCompany = new InsuranceCompany("Company1");

        insuranceCompanyService.createInsuranceCompany(insuranceCompany);

        List<InsuranceCompany> insuranceCompaniesBefore = (List<InsuranceCompany>) insuranceCompanyRepository.findAll();

        int sizeBefore = insuranceCompaniesBefore.size();

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(insuranceCompany))
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        List<InsuranceCompany> insuranceCompaniesAfter = (List<InsuranceCompany>) insuranceCompanyRepository.findAll();

        int sizeAfter = insuranceCompaniesAfter.size();

        assertEquals(sizeBefore, sizeAfter);

    }

    @Test
    void getAllInsuranceCompanies() throws Exception {

        final var all = insuranceCompanyService.findAll();

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
    void getEmptyAllInsuranceCompanies() throws Exception {

        insuranceCompanyRepository.deleteAll();

        final var emptyList = Collections.emptyList();

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
    void deleteInsuranceCompany() throws Exception {
        final var insuranceCompany = createRandomInsuranceCompany();
        final var savedInsuranceCompany = insuranceCompanyService.createInsuranceCompany(insuranceCompany);

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + savedInsuranceCompany.getId())
                )
                .andExpect(status().isNoContent());

        final var exception = assertThrows(ResourceNotFoundException.class,
                () -> insuranceCompanyService.findById(savedInsuranceCompany.getId())
        );
        assertEquals("Insurance company not found.", exception.getMessage());

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + savedInsuranceCompany.getId())
                )
                .andExpect(status().isNotFound());

        // Delete non existing insurance company

        final var insuranceCompanyMockID = 1000L;

        final var before = insuranceCompanyService.findAll();

        final var exception2 = assertThrows(ResourceNotFoundException.class,
                () -> insuranceCompanyService.deleteInsuranceCompanyById(insuranceCompanyMockID)
        );
        assertEquals("Insurance company not found.", exception2.getMessage());

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + insuranceCompanyMockID)
                )
                .andExpect(status().isNotFound());
        final var after = insuranceCompanyService.findAll();

        assertEquals("Size should be the same", before.size(), after.size());


    }

    @Test
    public void updateInsuranceCompany() throws Exception {
        final var insuranceCompany = createRandomInsuranceCompany();
        final var insuranceCompanyUpdated = createRandomInsuranceCompany();

        final var id = insuranceCompanyService.createInsuranceCompany(insuranceCompany).getId();

        assertEquals(toJson(insuranceCompany), toJson(insuranceCompanyService.findById(id)));

        mockMvc
                .perform(
                        put(urlBase + "/update/" + id)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(insuranceCompanyUpdated))
                )
                .andExpect(status().isOk());

        insuranceCompanyUpdated.setId(id);
        assertEquals(toJson(insuranceCompanyUpdated), toJson(insuranceCompanyService.findById(id)));

        long mockID = 1000L;

        final var before = insuranceCompanyService.findAll();

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> insuranceCompanyService.updateInsuranceCompany(mockID, insuranceCompanyUpdated)
        );
        assertEquals("Insurance company not found.", exception.getMessage());

        mockMvc
                .perform(
                        put(urlBase + "/update/" + mockID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(insuranceCompanyUpdated))
                )
                .andExpect(status().isNotFound());

        final var after = insuranceCompanyService.findAll();

        assertEquals("Size should be the same", before.size(), after.size());
    }

    @Test
    void getInsuranceCompanyClients() throws Exception {
        InsuranceCompany insuranceCompany = createRandomInsuranceCompany();
        long insuranceCompanyId = insuranceCompanyService.createInsuranceCompany(insuranceCompany).getId();
        List<Client> clients = insuranceCompanyService.getInsuranceCompanyClients(insuranceCompanyId);
        long insuranceCompanyMockId = 1000L;

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase + "/" + insuranceCompanyId + "/get-clients")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        Assert.assertEquals("Size should be the same", clients.size(), list.size());

        mockMvc
                .perform(
                        get(urlBase + "/" + insuranceCompanyMockId + "/get-clients")
                )
                .andExpect(status().isNotFound());
    }
}
