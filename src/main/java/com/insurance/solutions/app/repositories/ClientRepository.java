package com.insurance.solutions.app.repositories;

import com.insurance.solutions.app.models.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    boolean existsByDniAndInsuranceCompany(String dni, String insuranceCompany);

}
