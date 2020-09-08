package com.insurance.solutions.app.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table
public class Vehicle {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "License plate can not be blank")
    private String licensePlate;

    private ENUM_CATEGORY category;

    @NotBlank(message = "Brand can not be blank")
    private String brand;

    @NotBlank(message = "Model can not be blank")
    private String model;

    @NotBlank(message = "Driving profile can not be blank")
    private String drivingProfiles;

    @NotBlank(message = "Driving profile can not be blank")
    private String monitoringSystems;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    public Vehicle() {
    }

    public Vehicle(String licensePlate, ENUM_CATEGORY category, String brand, String model, String drivingProfiles, String monitoringSystems) {
        this.licensePlate = licensePlate;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.drivingProfiles = drivingProfiles;
        this.monitoringSystems = monitoringSystems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public ENUM_CATEGORY getCategory() {
        return category;
    }

    public void setCategory(ENUM_CATEGORY category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDrivingProfiles() {
        return drivingProfiles;
    }

    public void setDrivingProfiles(String drivingProfiles) {
        this.drivingProfiles = drivingProfiles;
    }

    public String getMonitoringSystems() {
        return monitoringSystems;
    }

    public void setMonitoringSystems(String monitoringSystems) {
        this.monitoringSystems = monitoringSystems;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}


