package com.insurance.solutions.app.repositories;

import com.insurance.solutions.app.models.DrivingProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DrivingProfileRepository extends CrudRepository<DrivingProfile, Long> {


}
