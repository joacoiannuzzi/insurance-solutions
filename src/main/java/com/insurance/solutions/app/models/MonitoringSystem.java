package com.insurance.solutions.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table
public class MonitoringSystem {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Name can not be blank")
    private String name;
    private String monitoringCompany;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    private boolean isAssigned;

    public MonitoringSystem() {
    }

    public MonitoringSystem(String name, String monitoringCompany) {
        this.name = name;
        this.monitoringCompany = monitoringCompany;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getMonitoringCompany() {
        return monitoringCompany;
    }

    public void setMonitoringCompany(String monitoringCompany) {
        this.monitoringCompany = monitoringCompany;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {

        this.vehicle = vehicle;
    }

    public boolean getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(boolean assigned) {
        isAssigned = assigned;
    }
}
