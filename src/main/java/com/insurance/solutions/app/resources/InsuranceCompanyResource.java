package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class InsuranceCompanyResource {
    Long id;
    String name;
    List<ClientResource> clients;
    List<UserResource> users;

    public InsuranceCompanyResource(@JsonProperty("id") Long id,
                                    @JsonProperty("name") String name,
                                    @JsonProperty("clients") List<ClientResource> clients,
                                    @JsonProperty("users") List<UserResource> users) {
        this.id = id;
        this.name = name;
        this.clients = clients;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ClientResource> getClients() {
        return clients;
    }

    public List<UserResource> getUsers() {
        return users;
    }
}
