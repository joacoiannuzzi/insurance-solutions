package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.models.InsuranceCompany;
import com.insurance.solutions.app.repositories.InsuranceCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsuranceCompanyService {

    @Autowired
    private InsuranceCompanyRepository insuranceCompanyRepository;

    public InsuranceCompany createInsuranceCompany(InsuranceCompany insuranceCompany) {
        if (insuranceCompanyRepository.existsByName(insuranceCompany.getName()))
            throw new BadRequestException("An insurance company with name: " + insuranceCompany.getName() + " already exists.");

        return insuranceCompanyRepository.save(insuranceCompany);
    }

}
