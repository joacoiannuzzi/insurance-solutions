package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class ClientResource {
    private final Long id;
    private final String dni;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String mail;
    private final String insuranceCompany;
    private final List<VehicleResource> vehicles;

    public ClientResource(@JsonProperty("id") Long id,
                          @JsonProperty("dni") String dni,
                          @JsonProperty("firstName") String firstName,
                          @JsonProperty("lastName") String lastName,
                          @JsonProperty("phoneNumber") String phoneNumber,
                          @JsonProperty("mail") String mail,
                          @JsonProperty("insuranceCompany") String insuranceCompany,
                          @JsonProperty("vehicles") List<VehicleResource> vehicles) {
        this.id = id;
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.insuranceCompany = insuranceCompany;
        this.vehicles = vehicles;
    }

    public Long getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public List<VehicleResource> getVehicles() {
        return vehicles;
    }
}
