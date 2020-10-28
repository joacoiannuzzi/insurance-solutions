package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.*;
import com.insurance.solutions.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }


    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

//    public List<User> getAllBase() {
//        return (List<User>) userRepository.findAll();
//    }

    public void deleteUser(Long userId) {
        final var user = findById(userId);
        userRepository.delete(user);
    }

}
