package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.User;
import com.insurance.solutions.app.models.enums.UserRole;
import com.insurance.solutions.app.resources.UserResource;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {
    public static List<UserResource> makeUsers(List<User> users) {
        List<UserResource> userResources = new ArrayList<>();
        for (User user : users)
            userResources.add(makeUser(user));
        return userResources;
    }

    public static UserResource makeUser(User user) {
        if (user == null) return null;

        UserRole role = user.getRole();
        return new UserResource(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                role != null ? role.name() : null
        );
    }
}
