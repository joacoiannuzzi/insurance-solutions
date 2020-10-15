package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.models.InsuranceCompany;
import com.insurance.solutions.app.repositories.InsuranceCompanyRepository;
import com.insurance.solutions.app.services.InsuranceCompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
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
}
