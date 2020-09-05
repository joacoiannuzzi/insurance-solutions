package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.repositories.ClientRepository;
import com.insurance.solutions.app.services.ClientService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientControllerTest {

    String urlBase = "/clients";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    @BeforeEach
    public void resetClients() {
        clientRepository.saveAll(
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
    void createValidClient() throws Exception {
        Client client = new Client("1", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro", "Auto");

        MvcResult response = mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        long id = objectMapper.readValue(response.getResponse().getContentAsString(), Client.class).getId();
        client.setId(id);

        Assert.assertEquals(toJson(client), toJson(clientService.getClientById(id)));

    }

    @Test
    void createInvalidClient() throws Exception {
        Client client = new Client();
        client.setDni("1");

        int clientQuantity = clientService.findAll().size();

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                )
                .andExpect(status().isBadRequest());

        Assert.assertEquals(clientQuantity, clientService.findAll().size());

    }

    @Test
    void createExistingClient() throws Exception {
        Client client = new Client("2", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 2", "Auto");

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                );

        int clientQuantity = clientService.findAll().size();

        mockMvc
                .perform(
                        post(urlBase + "/create")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(client))
                )
                .andExpect(status().isBadRequest());

        Assert.assertEquals(clientQuantity, clientService.findAll().size());

    }

    @Test
    void getClientById() throws Exception {
        Client client = new Client("3", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 3", "Auto");

        Client newClient = clientService.createClient(client);

        MvcResult response = mockMvc
                .perform(get("/clients/get/" + newClient.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(toJson(client)))
                .andReturn();

        Assert.assertEquals(toJson(newClient), response.getResponse().getContentAsString());

        long mockID = 100L;

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(mockID));
        Assert.assertEquals("Client not found.", exception.getMessage());

        mockMvc
                .perform(get("/clients/get/" + mockID))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteValidClient() throws Exception {

        long idToDelete = clientRepository.findAll().iterator().next().getId();

        mockMvc
                .perform(
                        delete(urlBase + "/" + idToDelete)
                )
                .andExpect(status().isOk());

        assertTrue(clientRepository.findById(idToDelete).isEmpty());
    }

    @Test
    void deleteInvalidClient() throws Exception {

        long idToDelete = 57775786867867878L;

        mockMvc
                .perform(
                        delete(urlBase + "/" + idToDelete)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllClients() throws Exception {

        Iterable<Client> all = clientRepository.findAll();

        mockMvc
                .perform(
                        get(urlBase)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(all)));

    }

    @Test
    void getEmptyAllClients() throws Exception {

        clientRepository.deleteAll();

        List<Object> emptyList = Collections.emptyList();

        mockMvc
                .perform(
                        get(urlBase)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(emptyList)));

    }

}