package com.omarserrar.textme.services;

import com.omarserrar.textme.models.user.SessionRepository;
import com.omarserrar.textme.models.user.UserSessions;
import com.omarserrar.textme.services.responses.LoginResponse;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.models.user.requests.LoginRequest;
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
    SessionRepository sessionRepository;
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
    public LoginResponse login(LoginRequest loginRequest){
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            User user = userRepository.findUserByUsername(loginRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null)
            );
            String jwt = JWTUtils.getUserJWT(user);
            UserSessions session = sessionRepository.save(UserSessions.builder().jwt(jwt).build());
            sessionRepository.save(session);
            user.getSessions().add(session);
            userRepository.save(user);
            return LoginResponse.builder().JWT(jwt).build();
        }
        catch (AuthenticationException e){
            e.printStackTrace();
            return LoginResponse.builder().message(e.getMessage()).error(true).build();
        }
    }
}
