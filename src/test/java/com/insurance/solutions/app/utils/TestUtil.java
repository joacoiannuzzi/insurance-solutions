package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.*;
import com.insurance.solutions.app.models.enums.UserType;
import com.insurance.solutions.app.models.enums.VehicleCategory;

import java.util.Date;
import java.util.Random;

public class TestUtil {

    final static Random random = new Random();

    public static DrivingProfile createRandomDrivingProfile() {
        return new DrivingProfile(random.nextDouble(), random.nextDouble(), random.nextDouble(),
                random.nextDouble(), random.nextDouble(), new Date(random.nextInt()), new Date(random.nextInt()));
    }

    public static MonitoringSystem createRandomMonitoringSystem() {
        return new MonitoringSystem(
                String.valueOf(random.nextInt()),
                String.valueOf(random.nextInt()),
                String.valueOf(random.nextInt())
        );
    }

    private static VehicleCategory randomVehicleCategory() {
        final var values = VehicleCategory.values();
        return values[random.nextInt(values.length)];

    }

    public static Client createRandomClient() {
        return new Client(
                String.valueOf(random.nextInt()),
                String.valueOf(random.nextInt()),
                String.valueOf(random.nextInt()),
                String.valueOf(random.nextInt()),
                String.valueOf(random.nextInt())
        );
    }

    public static Vehicle createRandomVehicle() {
        return new Vehicle(String.valueOf(random.nextLong()), randomVehicleCategory(),
                String.valueOf(random.nextLong()), String.valueOf(random.nextLong()));
    }

    public static InsuranceCompany createRandomInsuranceCompany() {
        return new InsuranceCompany(String.valueOf(random.nextInt()));
    }

    private static UserType randomUserType() {
        final var values = UserType.values();
        return values[random.nextInt(values.length)];

    }
    public static User createRandomUser() {
        return new User(
                String.valueOf(random.nextInt()),
                String.valueOf(random.nextInt()),
                String.valueOf(random.nextInt()),
                randomUserType()
        );
    }


}
