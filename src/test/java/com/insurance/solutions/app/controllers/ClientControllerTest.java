package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.repositories.ClientRepository;
import com.insurance.solutions.app.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
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

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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

    private <T> T toClass(MvcResult response, Class<T> type) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getResponse().getContentAsString(), type);
    }

    @BeforeEach
    public void resetClients() {
        clientRepository.saveAll(
                List.of(
                        new Client("4564564", "Annie", "Sims", "(006)-902-3913",
                                "annie.sims@example.com", "segur car"),

                        new Client("342342", "Leo", "Lucas", "(304)-520-6733",
                                "leo.lucas@example.com", "osde"),

                        new Client("5252342", "Olivia", "Pena", "(097)-119-2103",
                                "olivia.pena@example.com", "LA CUEVA MOTOS"),

                        new Client("345345343", "Clayton", "Murphy", "(237)-281-4159",
                                "clayton.murphy@example.com", "Seguros Russo / Taller Felix")

                )
        );
    }

    @Test
    void createValidClient() throws Exception {
        Client client = new Client("1", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro");
        client.setId(100L);

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

        long id = toClass(response, Client.class).getId();
        client.setId(id);

        assertEquals(toJson(client), toJson(clientService.getClientById(id)));

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

        assertEquals(clientQuantity, clientService.findAll().size());

    }

    @Test
    void createExistingClient() throws Exception {
        Client client = new Client("2", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 2");

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

        assertEquals(clientQuantity, clientService.findAll().size());

    }

    @Test
    void getClientById() throws Exception {
        Client client = new Client("3", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 3");

        Client newClient = clientService.createClient(client);

        MvcResult response = mockMvc
                .perform(get("/clients/get/" + newClient.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(toJson(client)))
                .andReturn();

        assertEquals(toJson(newClient), response.getResponse().getContentAsString());

        long mockID = 100L;

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(mockID));
        assertEquals("Client not found.", exception.getMessage());

        mockMvc
                .perform(get("/clients/get/" + mockID))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteValidClient() throws Exception {

        long idToDelete = clientService.findAll().get(0).getId();

        mockMvc
                .perform(
                        delete(urlBase + "/" + idToDelete)
                )
                .andExpect(status().isOk());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(idToDelete));
        assertEquals("Client not found.", exception.getMessage());
    }

    @Test
    void deleteInvalidClient() throws Exception {

        List<Client> before = clientService.findAll();

        long idToDelete = 57775786867867878L;

        mockMvc
                .perform(
                        delete(urlBase + "/" + idToDelete)
                )
                .andExpect(status().isNotFound());

        List<Client> after = clientService.findAll();

        assertEquals("Size should be the same", before.size(), after.size());
    }

    @Test
    void getAllClients() throws Exception {

        List<Client> all = clientService.findAll();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(all)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        Assert.assertEquals("Size should be the same", all.size(), list.size());

    }

    @Test
    void getEmptyAllClients() throws Exception {

        clientRepository.deleteAll();

        List<Client> all = clientService.findAll();

        List<Object> emptyList = Collections.emptyList();

        MvcResult mvcResult = mockMvc
                .perform(
                        get(urlBase)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(toJson(emptyList)))
                .andReturn();

        List list = toClass(mvcResult, List.class);

        Assert.assertEquals("Size should be the same", all.size(), list.size());

    }

    @Test
    public void updateClients() throws Exception {
        Client client = new Client("4", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 4");
        Client updatedClient = new Client("5", "Juan", "Perez", "123",
                "juanperez@mail.com", "Seguro 5");

        Long id = clientService.createClient(client).getId();

        Assert.assertEquals(toJson(client), toJson(clientService.getClientById(id)));

        mockMvc
                .perform(
                        put("/clients/update/" + id)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(updatedClient))
                )
                .andExpect(status().isOk());

        updatedClient.setId(id);
        Assert.assertEquals(toJson(updatedClient), toJson(clientService.getClientById(id)));

        long mockID = 100L;

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> clientService.updateClient(mockID, updatedClient));
        Assert.assertEquals("Client not found.", exception.getMessage());

        mockMvc
                .perform(
                        put("/clients/update/" + mockID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(updatedClient))
                )
                .andExpect(status().isNotFound());
    }

}