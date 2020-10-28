package com.insurance.solutions.app.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

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


    @OneToMany(mappedBy = "vehicle",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<DrivingProfile> drivingProfiles = new HashSet<>();

    @OneToOne(mappedBy = "vehicle",
            cascade = CascadeType.ALL)
    private MonitoringSystem monitoringSystem;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Vehicle() {
    }

    public Vehicle(String licensePlate, ENUM_CATEGORY category, String brand, String model) {
        this.licensePlate = licensePlate;
        this.category = category;
        this.brand = brand;
        this.model = model;
    }

    public void addDrivingProfile(DrivingProfile drivingProfile) {
        drivingProfiles.add(drivingProfile);
    }

    public void removeDrivingProfile(DrivingProfile drivingProfile) {
        drivingProfiles.remove(drivingProfile);
    }

    public void setDrivingProfiles(Set<DrivingProfile> drivingProfiles) {
        this.drivingProfiles = drivingProfiles;
    }

    public Set<DrivingProfile> getDrivingProfiles() {
        return drivingProfiles;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public MonitoringSystem getMonitoringSystem() {
        return monitoringSystem;
    }

    public void setMonitoringSystem(MonitoringSystem monitoringSystem) {
        this.monitoringSystem = monitoringSystem;
    }
}


