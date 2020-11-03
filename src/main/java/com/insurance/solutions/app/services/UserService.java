package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.*;
import com.insurance.solutions.app.models.enums.UserRole;
import com.insurance.solutions.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }


    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

    public List<User> getAllBase() {
        return ((List<User>) userRepository.findAll())
                .stream()
                .filter(user -> user.getRole().equals(UserRole.ROLE_BASE))
                .collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        final var user = findById(userId);
        userRepository.delete(user);
    }

}
