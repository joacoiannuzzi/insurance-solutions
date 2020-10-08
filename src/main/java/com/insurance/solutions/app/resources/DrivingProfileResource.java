package com.insurance.solutions.app.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Date;

@Value
public class DrivingProfileResource {
    private final Long id;
    private final double AvgSpeed;
    private final double maxSpeed;
    private final double minSpeed;
    private final double totalDrivingTime;
    private final double AvgDailyDrivingTime;
    private final Date startDate;
    private final Date finishDate;
    private final VehicleResource vehicle;

    public DrivingProfileResource(@JsonProperty("id") Long id,
                                  @JsonProperty("avgSpeed") double avgSpeed,
                                  @JsonProperty("maxSpeed") double maxSpeed,
                                  @JsonProperty("minSpeed") double minSpeed,
                                  @JsonProperty("totalDrivingTime") double totalDrivingTime,
                                  @JsonProperty("avgDailyDrivingTime") double avgDailyDrivingTime,
                                  @JsonProperty("startDate") Date startDate,
                                  @JsonProperty("finishDate") Date finishDate,
                                  @JsonProperty("vehicle") VehicleResource vehicle) {
        this.id = id;
        AvgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        this.totalDrivingTime = totalDrivingTime;
        AvgDailyDrivingTime = avgDailyDrivingTime;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.vehicle = vehicle;
    }

    public Long getId() {
        return id;
    }

    public double getAvgSpeed() {
        return AvgSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public double getTotalDrivingTime() {
        return totalDrivingTime;
    }

    public double getAvgDailyDrivingTime() {
        return AvgDailyDrivingTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public VehicleResource getVehicle() {
        return vehicle;
    }
}
