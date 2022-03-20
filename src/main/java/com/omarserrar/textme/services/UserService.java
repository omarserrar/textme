package com.omarserrar.textme.services;

import com.omarserrar.textme.user.User;
import com.omarserrar.textme.user.UserRepository;
import com.omarserrar.textme.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> getUserList(){
        return userRepository.findAll();
    }
    public User  getUser(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException());
    }
    public User addUser(User user){
        return userRepository.save(user);
    }


}
