package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.models.ENUM_CATEGORY;
import com.insurance.solutions.app.models.Vehicle;

import java.util.Date;
import java.util.Random;

public class TestUtil {

    final static Random random = new Random();

    public static DrivingProfile createRandomDrivingProfile() {
        return new DrivingProfile(random.nextDouble(), random.nextDouble(), random.nextDouble(),
                random.nextDouble(), random.nextDouble(), new Date(random.nextInt()), new Date(random.nextInt()));
    }

    private static ENUM_CATEGORY randomVehicleCategory() {
        final var values = ENUM_CATEGORY.values();
        return values[random.nextInt(values.length)];

    }

    public static Vehicle createRandomVehicle() {
        return new Vehicle(String.valueOf(random.nextLong()), randomVehicleCategory(),
                String.valueOf(random.nextLong()), String.valueOf(random.nextLong()));
    }
}
