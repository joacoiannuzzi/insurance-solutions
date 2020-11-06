package com.insurance.solutions.app.controllers;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.utils.Authentication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.insurance.solutions.app.security.SecurityConstants.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserAuthenticationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    @Test
    void userLogin() throws Exception {
        Authentication valid = new Authentication("Jose", "model");
        Authentication invalidUsername = new Authentication("invalid", "model");
        Authentication invalidPassword = new Authentication("Jose", "invalid");
        Authentication invalid = new Authentication("invalid", "invalid");

        MvcResult response = mockMvc
                .perform(
                        post("/login")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(valid))
                )
                .andExpect(status().isOk())
                .andReturn();

        String token = response.getResponse().getHeader("Authorization").replace(TOKEN_PREFIX, "");
        String expectedToken = JWT.create().withSubject(valid.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        assertEquals(expectedToken, token);

        mockMvc
                .perform(
                        post("/login")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(invalidUsername))
                )
                .andExpect(status().isUnauthorized())
                .andReturn();

        mockMvc
                .perform(
                        post("/login")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(invalidPassword))
                )
                .andExpect(status().isUnauthorized())
                .andReturn();

        mockMvc
                .perform(
                        post("/login")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(invalid))
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

}
