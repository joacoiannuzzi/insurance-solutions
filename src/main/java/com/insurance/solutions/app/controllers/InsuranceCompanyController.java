package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.InsuranceCompany;
import com.insurance.solutions.app.resources.InsuranceCompanyResource;
import com.insurance.solutions.app.services.InsuranceCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.insurance.solutions.app.utils.InsuranceCompanyUtils.makeInsuranceCompanies;
import static com.insurance.solutions.app.utils.InsuranceCompanyUtils.makeInsuranceCompany;

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
        return ResponseEntity.ok(makeInsuranceCompanies(insuranceCompanyService.findAll(), true));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteInsuranceCompany(@PathVariable Long id) {
        insuranceCompanyService.deleteInsuranceCompanyById(id);
        return ResponseEntity.noContent().build();
    }
}
