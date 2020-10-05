package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class MonitoringSystemResource {
    private final Long id;
    private final String name;
    private final String sensor;
    private final String monitoringCompany;
    private final boolean isAssigned;

    public MonitoringSystemResource(@JsonProperty("id") Long id,
                                    @JsonProperty("name") String name,
                                    @JsonProperty("sensor") String sensor,
                                    @JsonProperty("monitoringCompany") String monitoringCompany,
                                    @JsonProperty("isAssigned") boolean isAssigned) {
        this.id = id;
        this.name = name;
        this.sensor = sensor;
        this.monitoringCompany = monitoringCompany;
        this.isAssigned = isAssigned;
    }
}
