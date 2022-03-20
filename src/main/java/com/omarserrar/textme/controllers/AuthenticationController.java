package com.omarserrar.textme.controllers;

import com.omarserrar.textme.services.AuthenticationService;
import com.omarserrar.textme.services.UserService;
import com.omarserrar.textme.user.User;
import com.omarserrar.textme.user.requests.LoginRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
    @Autowired
    public AuthenticationService authenticationService;

    @PostMapping("register")
    private ResponseEntity<User> createUser(@RequestBody User user){
        System.out.println("here");
        return ResponseEntity.ok(authenticationService.createUser(user));
    }
    @PostMapping("login")
    private ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

}