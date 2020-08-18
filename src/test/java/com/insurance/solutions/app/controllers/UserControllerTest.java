package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.resources.UserResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAll() throws Exception {
        List<UserResource> userResources = new ArrayList<>();
        userResources.add(new UserResource(1L, "Sebastian", "sebastian@mail.com"));
        userResources.add(new UserResource(2L, "Tomas", "tomas@mail.com"));
        userResources.add(new UserResource(3L, "Franco", "franco@mail.com"));
        userResources.add(new UserResource(4L, "Jose", "jose@mail.com"));

        System.out.println("\n--------------------------");
        System.out.println("Expect");
        System.out.println(objectMapper.writeValueAsString(userResources));
        System.out.println("--------------------------\n");

        this.mockMvc
                .perform(get("/users/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(objectMapper.writeValueAsString(userResources)));
    }
}
