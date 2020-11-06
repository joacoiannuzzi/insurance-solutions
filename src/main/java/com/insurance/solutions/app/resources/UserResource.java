package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UserResource {
    private final Long id;
    private final String username;
    private final String email;
    private final String rol;

    public UserResource(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String username,
            @JsonProperty("email") String email,
            @JsonProperty("rol") String rol
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }
}
