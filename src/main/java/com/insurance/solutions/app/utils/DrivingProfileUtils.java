package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.models.Vehicle;
import com.insurance.solutions.app.resources.DrivingProfileResource;

import java.util.ArrayList;
import java.util.List;

import static com.insurance.solutions.app.utils.VehicleUtils.makeVehicle;

public class DrivingProfileUtils {
    public static List<DrivingProfileResource> makeDrivingProfiles(List<DrivingProfile> drivingProfiles, boolean relationship) {
        List<DrivingProfileResource> drivingProfileResources = new ArrayList<>();
        for (DrivingProfile drivingProfile : drivingProfiles)
            drivingProfileResources.add(makeDrivingProfile(drivingProfile, relationship));
        return drivingProfileResources;
    }

    public static DrivingProfileResource makeDrivingProfile(DrivingProfile drivingProfile, boolean relationship) {
        if (drivingProfile == null) return null;

        Vehicle vehicle = drivingProfile.getVehicle();
        return new DrivingProfileResource(
                drivingProfile.getId(),
                drivingProfile.getAvgSpeed(),
                drivingProfile.getMaxSpeed(),
                drivingProfile.getMinSpeed(),
                drivingProfile.getTotalDrivingTime(),
                drivingProfile.getAvgDailyDrivingTime(),
                drivingProfile.getStartDate(),
                drivingProfile.getFinishDate(),
                relationship && vehicle != null ? makeVehicle(vehicle, false) : null
        );
    }
}
