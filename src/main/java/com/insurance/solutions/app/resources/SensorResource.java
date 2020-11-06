package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class SensorResource {
    Long id;
    String name;
    String model;
    List<MonitoringSystemResource> monitoringSystems;

    public SensorResource(@JsonProperty("id") Long id,
                          @JsonProperty("name") String name,
                          @JsonProperty("model") String model,
                          @JsonProperty("monitoringSystems")List<MonitoringSystemResource> monitoringSystems) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.monitoringSystems = monitoringSystems;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public List<MonitoringSystemResource> getMonitoringSystems() {
        return monitoringSystems;
    }
}
