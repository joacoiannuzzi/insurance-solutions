package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.*;
import com.insurance.solutions.app.models.enums.UserRole;
import com.insurance.solutions.app.repositories.InsuranceCompanyRepository;
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
    private InsuranceCompanyRepository insuranceCompanyRepository;

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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
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

    public User assignInsuranceCompany(Long userId, Long insuranceCompanyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        InsuranceCompany insuranceCompany = insuranceCompanyRepository.findById(insuranceCompanyId).orElseThrow(() ->
                new ResourceNotFoundException("Insurance company not found."));
        user.setInsuranceCompany(insuranceCompany);
        return userRepository.save(user);
    }
}
