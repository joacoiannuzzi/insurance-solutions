package com.insurance.solutions.app.models;

import javax.persistence.*;

@Entity
@Table
public class MonitoringSystem {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String sensor;
    private String monitoringCompany;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    public MonitoringSystem() {
    }

    public MonitoringSystem(String name, String sensor, String monitoringCompany) {
        this.name = name;
        this.sensor = sensor;
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

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
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
}