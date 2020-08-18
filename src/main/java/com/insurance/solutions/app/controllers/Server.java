package com.insurance.solutions.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
@CrossOrigin
public class Server {

    @GetMapping("/_healthcheck")
    public ResponseEntity<String> getAllProducts() {
        return new ResponseEntity<>("This server is working!", HttpStatus.OK);
    }
}
