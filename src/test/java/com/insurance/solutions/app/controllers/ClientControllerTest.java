package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientService clientService;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    @BeforeEach
    public void resetClients() {
        clientService.deleteAll();
        clientService.saveAll(
                List.of(
                        new Client("4564564", "Annie", "Sims", "(006)-902-3913",
                                "annie.sims@example.com", "segur car", "ford falcon"),

                        new Client("342342", "Leo", "Lucas", "(304)-520-6733",
                                "leo.lucas@example.com", "osde", "tesla"),

                        new Client("5252342", "Olivia", "Pena", "(097)-119-2103",
                                "olivia.pena@example.com", "LA CUEVA MOTOS", "ferrari"),

                        new Client("345345343", "Clayton", "Murphy", "(237)-281-4159",
                                "clayton.murphy@example.com", "Seguros Russo / Taller Felix", "moto")

                )
        );
    }

    @Test
    void deleteValidClient() throws Exception {

        long idToDelete = clientService.findAll().iterator().next().getId();

        mockMvc
                .perform(
                        delete("/clients/" + idToDelete)
                )
                .andExpect(status().isOk());

       assertTrue(clientService.findById(idToDelete).isEmpty());
    }

    @Test
    void deleteInvalidClient() throws Exception {

        long idToDelete = 57775786867867878L;

        mockMvc
                .perform(
                        delete("/clients/" + idToDelete)
                )
                .andExpect(status().isBadRequest());

    }

}