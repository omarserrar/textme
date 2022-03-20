package com.omarserrar.textme.services;

import com.omarserrar.textme.user.User;
import com.omarserrar.textme.user.UserRepository;
import com.omarserrar.textme.user.requests.LoginRequest;
import com.omarserrar.textme.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    public Optional<User> getCurrentUser(){
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.of(user);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user);
        return userService.addUser(user);
    }
    public String login(LoginRequest loginRequest){
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            User user = userRepository.findUserByUsername(loginRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null)
            );
            return JWTUtils.getUserJWT(user);
        }
        catch (AuthenticationException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
