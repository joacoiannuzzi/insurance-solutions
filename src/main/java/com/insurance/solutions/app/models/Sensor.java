package com.insurance.solutions.app.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotBlank(message = "Model can not be blank")
    private String model;

    @OneToMany(mappedBy = "sensor",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private final Set<MonitoringSystem> monitoringSystems = new HashSet<>();

    public Sensor() {
    }

    public Sensor(String name, String model) {
        this.name = name;
        this.model = model;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Set<MonitoringSystem> getMonitoringSystems() {
        return monitoringSystems;
    }

    public void addMonitoringSystem(MonitoringSystem monitoringSystem) {
        monitoringSystems.add(monitoringSystem);
    }

    public void removeMonitoringSystem(MonitoringSystem monitoringSystem) {
        monitoringSystems.remove(monitoringSystem);
    }
}
