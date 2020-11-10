package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.InsuranceCompany;
import com.insurance.solutions.app.models.User;
import com.insurance.solutions.app.models.enums.UserRole;
import com.insurance.solutions.app.resources.UserResource;

import java.util.ArrayList;
import java.util.List;

import static com.insurance.solutions.app.utils.InsuranceCompanyUtils.makeInsuranceCompany;

public class UserUtils {
    public static List<UserResource> makeUsers(List<User> users, boolean relationship) {
        List<UserResource> userResources = new ArrayList<>();
        for (User user : users)
            userResources.add(makeUser(user, relationship));
        return userResources;
    }

    public static UserResource makeUser(User user, boolean relationship) {
        if (user == null) return null;

        UserRole role = user.getRole();
        InsuranceCompany insuranceCompany = user.getInsuranceCompany();

        return new UserResource(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                role != null ? role.name() : null,
                relationship && insuranceCompany != null ? makeInsuranceCompany(insuranceCompany, false) : null
        );
    }
}
