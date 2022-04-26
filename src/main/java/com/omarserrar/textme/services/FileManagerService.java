package com.omarserrar.textme.services;

import com.omarserrar.textme.models.user.Image;
import com.omarserrar.textme.models.user.ImageRepository;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class FileManagerService {
    @Autowired
    ImageRepository imageRepository;
    public String getTempKey(){
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert connectedUser != null;

        return JWTUtils.getTemporaryFileReadKey(connectedUser);
    }

    public Image getFile(Long id) throws FileNotFoundException {
        return imageRepository.findById(id).orElseThrow(() -> new FileNotFoundException()) ;
    }
}
