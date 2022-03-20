package com.omarserrar.textme.controllers;

import com.omarserrar.textme.services.AuthenticationService;
import com.omarserrar.textme.services.UserService;
import com.omarserrar.textme.user.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    public UserService userService;
    @Autowired
    public AuthenticationService authenticationService;
    @GetMapping()
    private ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getUserList());
    }
    @GetMapping("me")
    private ResponseEntity<User> whoiam(){
        return ResponseEntity.ok(authenticationService.getCurrentUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

}
