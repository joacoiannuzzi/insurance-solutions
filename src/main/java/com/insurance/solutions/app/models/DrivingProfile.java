package com.insurance.solutions.app.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Table
@Entity
public class DrivingProfile {

    @Id
    @GeneratedValue
    private Long id;

    private double AvgSpeed;
    private double maxSpeed;
    private double minSpeed;
    private double totalDrivingTime;
    private double AvgDailyDrivingTime;
    private Date startDate;
    private Date finishDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    public DrivingProfile(double avgSpeed, double maxSpeed, double minSpeed, double totalDrivingTime, double avgDailyDrivingTime, Date startDate, Date finishDate) {
        AvgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        this.totalDrivingTime = totalDrivingTime;
        AvgDailyDrivingTime = avgDailyDrivingTime;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public DrivingProfile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAvgSpeed() {
        return AvgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        AvgSpeed = avgSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public double getTotalDrivingTime() {
        return totalDrivingTime;
    }

    public void setTotalDrivingTime(double totalDrivingTime) {
        this.totalDrivingTime = totalDrivingTime;
    }

    public double getAvgDailyDrivingTime() {
        return AvgDailyDrivingTime;
    }

    public void setAvgDailyDrivingTime(double avgDailyDrivingTime) {
        AvgDailyDrivingTime = avgDailyDrivingTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

}
