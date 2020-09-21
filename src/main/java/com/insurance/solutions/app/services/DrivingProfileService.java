package com.insurance.solutions.app.services;

import com.insurance.solutions.app.models.DrivingProfile;
import com.insurance.solutions.app.repositories.DrivingProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrivingProfileService {

    @Autowired
    private DrivingProfileRepository drivingProfileRepository;


    public DrivingProfile createDrivingProfile(DrivingProfile drivingProfile) {
        return drivingProfileRepository.save(drivingProfile);
    }
}
