package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.InsuranceCompany;
import com.insurance.solutions.app.resources.InsuranceCompanyResource;

import java.util.ArrayList;
import java.util.List;

import static com.insurance.solutions.app.utils.ClientUtils.makeClients;

public class InsuranceCompanyUtils {
    public static List<InsuranceCompanyResource> makeInsuranceCompanies(List<InsuranceCompany> insuranceCompanies, boolean relationship) {
        List<InsuranceCompanyResource> insuranceCompaniesResources = new ArrayList<>();
        for (InsuranceCompany insuranceCompany : insuranceCompanies) insuranceCompaniesResources.add(makeInsuranceCompany(insuranceCompany, relationship));
        return insuranceCompaniesResources;
    }

    public static InsuranceCompanyResource makeInsuranceCompany(InsuranceCompany insuranceCompany, boolean relationship) {
        if (insuranceCompany == null) return null;

        return new InsuranceCompanyResource(
                insuranceCompany.getId(),
                insuranceCompany.getName(),
                relationship ? makeClients(new ArrayList<>(insuranceCompany.getClients()), false) : new ArrayList<>()
        );
    }
}
