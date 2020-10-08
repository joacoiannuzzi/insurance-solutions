package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.insurance.solutions.app.models.ENUM_CATEGORY;
import lombok.Value;

import java.util.List;

@Value
public class VehicleResource {
    private final Long id;
    private final String licensePlate;
    private final ENUM_CATEGORY category;
    private final String brand;
    private final String model;
    private final List<DrivingProfileResource> drivingProfiles;
    private final MonitoringSystemResource monitoringSystem;
    private final ClientResource client;

    public VehicleResource(@JsonProperty("id") Long id,
                           @JsonProperty("licensePlate") String licensePlate,
                           @JsonProperty("category") ENUM_CATEGORY category,
                           @JsonProperty("brand") String brand,
                           @JsonProperty("model") String model,
                           @JsonProperty("drivingProfiles") List<DrivingProfileResource> drivingProfiles,
                           @JsonProperty("monitoringSystem") MonitoringSystemResource monitoringSystem,
                           @JsonProperty("client") ClientResource client) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.drivingProfiles = drivingProfiles;
        this.monitoringSystem = monitoringSystem;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public ENUM_CATEGORY getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public List<DrivingProfileResource> getDrivingProfiles() {
        return drivingProfiles;
    }

    public MonitoringSystemResource getMonitoringSystem() {
        return monitoringSystem;
    }

    public ClientResource getClient() {
        return client;
    }
}
