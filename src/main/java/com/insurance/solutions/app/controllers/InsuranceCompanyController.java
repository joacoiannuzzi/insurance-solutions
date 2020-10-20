package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.InsuranceCompany;
import com.insurance.solutions.app.resources.InsuranceCompanyResource;
import com.insurance.solutions.app.services.InsuranceCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.insurance.solutions.app.utils.InsuranceCompanyUtils.makeInsuranceCompanies;
import static com.insurance.solutions.app.utils.InsuranceCompanyUtils.makeInsuranceCompany;

import java.util.List;

@RestController
@RequestMapping("/insurance-companies")
@CrossOrigin
public class InsuranceCompanyController {

    @Autowired
    private InsuranceCompanyService insuranceCompanyService;

    @PostMapping("/create")
    public ResponseEntity<InsuranceCompanyResource> createInsuranceCompany(@Valid @RequestBody InsuranceCompany insuranceCompany) {
        return new ResponseEntity<>(makeInsuranceCompany(insuranceCompanyService.createInsuranceCompany(insuranceCompany),
                 true), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InsuranceCompanyResource>> getAllInsuranceCompanies() {
        return ResponseEntity.ok(makeInsuranceCompanies(insuranceCompanyService.getAllInsuranceCompanies(), true));
    }

    @GetMapping("/{insuranceCompanyId}/get-clients")
    public ResponseEntity<List<Client>> getInsuranceCompanyClients(@PathVariable Long insuranceCompanyId) {
        return new ResponseEntity<>(insuranceCompanyService.getInsuranceCompanyClients(insuranceCompanyId), HttpStatus.OK);
    }
}
