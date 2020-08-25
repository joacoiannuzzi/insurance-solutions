package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    @Test
    void createValidClient() throws Exception {
        Client client = new Client("1", "Juan", "Perez", "123", "juanperez@mail.com", "Seguro", "Auto");

        mockMvc
                .perform(
                        post("/clients/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber());

    }

    @Test
    void createInvalidClient() throws Exception {
        Client client = new Client();
        client.setDni("1");

        mockMvc
                .perform(
                        post("/clients/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    void createExistingClient() throws Exception {
        Client client = new Client("2", "Juan", "Perez", "123", "juanperez@mail.com", "Seguro 2", "Auto");

        mockMvc
                .perform(
                        post("/clients/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                );

        mockMvc
                .perform(
                        post("/clients/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                )
                .andExpect(status().isBadRequest());

    }
}
