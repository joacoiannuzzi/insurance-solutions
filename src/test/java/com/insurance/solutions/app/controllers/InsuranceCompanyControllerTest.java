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

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

        final var all = insuranceCompanyService.getAllInsuranceCompanies();

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
}
