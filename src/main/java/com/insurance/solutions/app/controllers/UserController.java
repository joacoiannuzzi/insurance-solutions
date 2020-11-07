package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.User;
import com.insurance.solutions.app.repositories.UserRepository;
import com.insurance.solutions.app.resources.UserResource;
import com.insurance.solutions.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.insurance.solutions.app.utils.UserUtils.makeUser;
import static com.insurance.solutions.app.utils.UserUtils.makeUsers;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResource>> getAllBaseUsers() {
        return ResponseEntity.ok().body(makeUsers(userService.getAll(), true));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{userId}/assign/{insuranceCompanyId}")
    public ResponseEntity<UserResource> assignInsuranceCompany(@PathVariable Long userId, @PathVariable Long insuranceCompanyId) {
        return new ResponseEntity<>(makeUser(userService.assignInsuranceCompany(userId, insuranceCompanyId), true), HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResource> updateUser(@PathVariable Long userId, @Valid @RequestBody User user) {
        return new ResponseEntity<>(makeUser(userService.updateUser(userId, user), true), HttpStatus.OK);
    }

}
