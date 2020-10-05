package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ClientResource {
    private final Long id;
    private final String dni;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String mail;
    private final String insuranceCompany;

    public ClientResource(@JsonProperty("id") Long id,
                          @JsonProperty("dni") String dni,
                          @JsonProperty("firstName") String firstName,
                          @JsonProperty("lastName") String lastName,
                          @JsonProperty("phoneNumber") String phoneNumber,
                          @JsonProperty("mail") String mail,
                          @JsonProperty("insuranceCompany") String insuranceCompany) {
        this.id = id;
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.insuranceCompany = insuranceCompany;
    }
}
