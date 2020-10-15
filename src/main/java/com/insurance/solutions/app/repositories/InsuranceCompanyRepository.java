package com.insurance.solutions.app.repositories;

import com.insurance.solutions.app.models.InsuranceCompany;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceCompanyRepository extends CrudRepository<InsuranceCompany, Long> {
    boolean existsByName(String name);
}
