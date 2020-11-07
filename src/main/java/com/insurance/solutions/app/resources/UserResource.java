package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UserResource {
    Long id;
    String username;
    String email;
    String rol;
    InsuranceCompanyResource insuranceCompany;

    public UserResource(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("rol") String rol,
            @JsonProperty("insuranceCompany") InsuranceCompanyResource insuranceCompany) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rol = rol;
        this.insuranceCompany = insuranceCompany;
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

    public InsuranceCompanyResource getInsuranceCompany() {
        return insuranceCompany;
    }
}
