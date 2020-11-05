package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class MonitoringSystemResource {
    Long id;
    String name;
    String monitoringCompany;
    boolean isAssigned;
    VehicleResource vehicle;
    SensorResource sensor;

    public MonitoringSystemResource(@JsonProperty("id") Long id,
                                    @JsonProperty("name") String name,
                                    @JsonProperty("monitoringCompany") String monitoringCompany,
                                    @JsonProperty("isAssigned") boolean isAssigned,
                                    @JsonProperty("vehicle") VehicleResource vehicle,
                                    @JsonProperty("sensor") SensorResource sensor) {
        this.id = id;
        this.name = name;
        this.monitoringCompany = monitoringCompany;
        this.isAssigned = isAssigned;
        this.vehicle = vehicle;
        this.sensor = sensor;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMonitoringCompany() {
        return monitoringCompany;
    }

    public boolean getIsAssigned() {
        return isAssigned;
    }

    public VehicleResource getVehicle() {
        return vehicle;
    }

    public SensorResource getSensor() {
        return sensor;
    }
}
