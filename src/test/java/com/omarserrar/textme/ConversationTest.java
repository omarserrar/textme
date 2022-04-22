package com.omarserrar.textme;

import com.omarserrar.textme.models.messenger.exceptions.ConversationNotFound;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.models.user.exceptions.ActionNotPermitted;
import com.omarserrar.textme.models.user.exceptions.NotAuth;
import com.omarserrar.textme.models.user.exceptions.UserNotFoundException;
import com.omarserrar.textme.services.MessengerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
public class ConversationTest {
    @Autowired
    MessengerService messengerService;
    @Autowired
    UserRepository userRepository;
    @BeforeEach
    public void init(){
        UserDetails userDetails = userRepository.findById(202L).orElseThrow();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @Test
    public void testGetConversationById() throws ActionNotPermitted, NotAuth, ConversationNotFound {
        Assertions.assertEquals(1, messengerService.getConversation(1L).getId());
    }
    @Test
    public void testGetConversationByUsers() throws NotAuth, UserNotFoundException, ConversationNotFound {
        Assertions.assertEquals(1, messengerService.getConversationWithUser(1L).getId());
    }
}
