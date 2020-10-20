package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.BadRequestException;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.InsuranceCompany;
import com.insurance.solutions.app.repositories.ClientRepository;
import com.insurance.solutions.app.repositories.InsuranceCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceCompanyService {

    @Autowired
    private InsuranceCompanyRepository insuranceCompanyRepository;

    @Autowired
    private ClientRepository clientRepository;

    public InsuranceCompany createInsuranceCompany(InsuranceCompany insuranceCompany) {
        if (insuranceCompanyRepository.existsByName(insuranceCompany.getName()))
            throw new BadRequestException("An insurance company with name: " + insuranceCompany.getName() + " already exists.");

        return insuranceCompanyRepository.save(insuranceCompany);
    }

    public List<InsuranceCompany> findAll() {
        return (List<InsuranceCompany>) insuranceCompanyRepository.findAll();
    }

    public InsuranceCompany findById(Long id) {
        return insuranceCompanyRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Insurance company not found.")
                );
    }



    public void deleteInsuranceCompanyById(Long id) {
        final var insuranceCompany = findById(id);
        insuranceCompany
                .getClients()
                .forEach(client -> {
                    insuranceCompany.removeClient(client);
                    client.setInsuranceCompany(null);
                    clientRepository.save(client);
                });
        insuranceCompanyRepository.save(insuranceCompany);
        insuranceCompanyRepository.deleteById(id);

    }
}
