package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.models.User;
import com.insurance.solutions.app.models.enums.UserType;
import com.insurance.solutions.app.repositories.UserRepository;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.resources.UserResource;
import com.insurance.solutions.app.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    String urlBase = "/users";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    @Test
    void createUser() throws Exception {
        User user = new User("User1", "user1@mail.com", "password", UserType.BASE);

        MvcResult response = mockMvc
                .perform(
                        post(urlBase + "/sign-up")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(user))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        User createdUser = objectMapper.readValue(response.getResponse().getContentAsString(), User.class);
        user.setId(createdUser.getId());
        user.setPassword(createdUser.getPassword());

        assertEquals(toJson(user), toJson(userRepository.findById(createdUser.getId())));

        User adminUser = new User("AdminUser1", "adminuser1@mail.com", "password", UserType.ADMIN);

        MvcResult response2 = mockMvc
                .perform(
                        post(urlBase + "/sign-up")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(toJson(adminUser))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        User createdAdminUser = objectMapper.readValue(response2.getResponse().getContentAsString(), User.class);
        adminUser.setId(createdAdminUser.getId());
        adminUser.setPassword(createdAdminUser.getPassword());

        assertEquals(toJson(adminUser), toJson(userRepository.findById(createdAdminUser.getId())));
    }

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

    @Test
    public void deleteExistingUser() throws Exception {
        User user = new User("User2", "user2@mail.com", "password", UserType.BASE);

        long userId = userService.createUser(user).getId();


        Assert.assertEquals(toJson(user), toJson(userService.findById(userId)));

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + userId)
                )
                .andExpect(status().isNoContent());

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> userService.findById(userId));
        Assert.assertEquals("User not found.", exception.getMessage());

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + userId)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNotExistingUser() throws Exception {

        List<User> before = userService.getAll();

        long idToDelete = 57775786867867878L;

        mockMvc
                .perform(
                        delete(urlBase + "/delete/" + idToDelete)
                )
                .andExpect(status().isNotFound());

        List<User> after = userService.getAll();

        assertEquals("Size should be the same", before.size(), after.size());
    }
}
