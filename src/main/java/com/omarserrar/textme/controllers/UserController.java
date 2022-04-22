package com.omarserrar.textme.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.omarserrar.textme.models.user.Image;
import com.omarserrar.textme.models.user.UserFilters;
import com.omarserrar.textme.models.user.exceptions.BadFileTypeException;
import com.omarserrar.textme.services.AuthenticationService;
import com.omarserrar.textme.services.UserService;
import com.omarserrar.textme.services.responses.ErrorResponse;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.exceptions.UserNotFoundException;
import jdk.jfr.ContentType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.ResourceBundle;

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
    private ResponseEntity whoiam(){
        User user = authenticationService.getCurrentUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(UserFilters.serializeAllExcept(user, "birthDate"));
    }

    @GetMapping("discover")
    private ResponseEntity<List<User>> discover(){
        User u = authenticationService.getCurrentUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(userService.discoverNewFriend(u));
    }

    @PostMapping("contact/{id}")
    private ResponseEntity addContact(@PathVariable("id") Long id){
        User u = authenticationService.getCurrentUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try{
            boolean r = userService.addContact(u,id);
            if(r)
                return ResponseEntity.noContent().build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User already in your contact."));
        }
        catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
        }

    }

    @DeleteMapping("contact/{id}")
    private ResponseEntity deleteContact(@PathVariable("id") Long id){
        User u = authenticationService.getCurrentUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try{
            boolean r = userService.deleteContact(u,id);
            if(r)
                return ResponseEntity.noContent().build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User not in your contact list."));
        }
        catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
        }
    }
}
