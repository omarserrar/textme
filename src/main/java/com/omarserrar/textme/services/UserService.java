package com.omarserrar.textme.services;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.omarserrar.textme.models.requests.UserEditRequest;
import com.omarserrar.textme.models.user.Image;
import com.omarserrar.textme.models.user.ImageRepository;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.models.user.exceptions.BadFileTypeException;
import com.omarserrar.textme.models.user.exceptions.NotAuth;
import com.omarserrar.textme.models.user.exceptions.UserEditException;
import com.omarserrar.textme.models.user.exceptions.UserNotFoundException;
import com.omarserrar.textme.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Lazy
    @Autowired
    NotificationService notificationService;
    public List<User> getUserList(){
        return userRepository.findAll();
    }
    public User  getUser(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException());
    }
    public User addUser(User user) throws Exception {
        if(user.getFirstName() == null || user.getFirstName().trim().length()<3 || user.getFirstName().trim().length()>20)
            throw new Exception("Invalid Firstname");
        if(user.getLastName() == null || user.getLastName().trim().length()<3 || user.getLastName().trim().length()>20)
            throw new Exception("Invalid Lastname");
        if(user.getGuest()==null || user.getGuest()==Boolean.FALSE){
            if(user.getUsername() == null || user.getUsername().trim().length()<3 || user.getUsername().trim().length()>20)
                throw new Exception("Invalid username");
            if(!userRepository.findUserByUsername(user.getUsername()).isEmpty())
                throw new Exception("Username already exist");
            if(user.getPassword()== null)
                throw new Exception("Invalid password");
            user.setGuest(false);
        }
        else{
            user.setUsername(null);
            user.setPassword(null);
        }
        user.setCreationDate(new Timestamp(System.currentTimeMillis()));
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
        notificationService.sendUserUpdateToAll(connectedUser);
        return true;
    }
    public User editUser(UserEditRequest userEditRequest) throws UserEditException {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert connectedUser!=null;
        if(!userEditRequest.validate()) throw new UserEditException("Cannot validate form");
        User u = userRepository.findUserByUsername(userEditRequest.getUsername().trim()).orElse(null);
        if(u!= null && u.getId() != connectedUser.getId())
            throw new UserEditException("Username already exist");
        connectedUser.setUsername(userEditRequest.getUsername());
        if(userEditRequest.getFirstName() != null)
            connectedUser.setFirstName(userEditRequest.getFirstName().trim());
        if(userEditRequest.getLastName() != null)
            connectedUser.setLastName(userEditRequest.getLastName().trim());
        if(userEditRequest.getPhoneNumber() != null)
            connectedUser.setPhoneNumber(userEditRequest.getPhoneNumber().trim());
        if(userEditRequest.getEmail() != null)
            connectedUser.setEmail(userEditRequest.getEmail().trim());
        notificationService.sendUserUpdateToAll(connectedUser);
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

    public User getUserFromJWT(String jwt){
        try{
            System.out.println("getUserFromJWT");
            long id = JWTUtils.getUserId(jwt);
            System.out.println("getUserFromJWT 2");
            return userRepository.findById(id).orElse(null);
        }
        catch (JWTDecodeException e){

        }
        catch (Exception e){

        }
        finally {
            System.out.println("getUserFromJWT Exception");
        }
        return null;
    }
}
