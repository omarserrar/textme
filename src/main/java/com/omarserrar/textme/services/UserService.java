package com.omarserrar.textme.services;

import com.omarserrar.textme.models.requests.UserEditRequest;
import com.omarserrar.textme.models.user.Image;
import com.omarserrar.textme.models.user.ImageRepository;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.models.user.exceptions.BadFileTypeException;
import com.omarserrar.textme.models.user.exceptions.NotAuth;
import com.omarserrar.textme.models.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;

    public List<User> getUserList(){
        return userRepository.findAll();
    }
    public User  getUser(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException());
    }
    public User addUser(User user){
        return userRepository.save(user);
    }

    public List<User> discoverNewFriend(User u){
        return userRepository.discoverUsers(u.getId());
    }

    public boolean addContact(User u, Long id) throws UserNotFoundException{
        User u2 = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        if(u.getContacts().contains(u2))
            return false;
        u.getContacts().add(u2);
        userRepository.save(u);
        return true;
    }

    public boolean deleteContact(User u, Long id) throws UserNotFoundException {
        User u2 = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        if(! u.getContacts().contains(u2))
            return false;
        u.getContacts().remove(u2);
        userRepository.save(u);
        return true;
    }

    public boolean updatePicture(MultipartFile file) throws AssertionError, BadFileTypeException, IOException {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert connectedUser!=null;

        if( ! file.getContentType().equals("image/jpeg") && ! file.getContentType().equals("image/png"))
            throw new BadFileTypeException();
        Image p = Image.builder()
                .fileName(file.getOriginalFilename())
                        .data(file.getBytes())
                                .type(file.getContentType()).build();
        imageRepository.save(p);
        connectedUser.setUserPicture(p);
        userRepository.save(connectedUser);
        return true;
    }
    public User editUser(UserEditRequest userEditRequest){
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert connectedUser!=null;

        if(userEditRequest.getUsername() != null)
            connectedUser.setUsername(userEditRequest.getUsername());
        if(userEditRequest.getFirstName() != null)
            connectedUser.setFirstName(userEditRequest.getFirstName());
        if(userEditRequest.getLastName() != null)
            connectedUser.setLastName(userEditRequest.getLastName());
        if(userEditRequest.getPhoneNumber() != null)
            connectedUser.setPhoneNumber(userEditRequest.getPhoneNumber());
        if(userEditRequest.getEmail() != null)
            connectedUser.setEmail(userEditRequest.getEmail());

        return userRepository.save(connectedUser);
    }
    public Image getUserPicture() throws UserNotFoundException {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert connectedUser!=null;
        return connectedUser.getUserPicture();
    }
    public Image getPicture(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException());
        return user.getUserPicture();
    }
}
