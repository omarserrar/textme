package com.omarserrar.textme.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.omarserrar.textme.models.messenger.Conversation;
import com.omarserrar.textme.models.messenger.Message;
import com.omarserrar.textme.models.messenger.MessengerRepository;
import com.omarserrar.textme.models.messenger.exceptions.ConversationNotFound;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserFilters;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.models.user.exceptions.ActionNotPermitted;
import com.omarserrar.textme.models.user.exceptions.NotAuth;
import com.omarserrar.textme.models.user.exceptions.UserNotFoundException;
import com.omarserrar.textme.services.MessengerService;
import com.omarserrar.textme.services.requests.MessageRequest;
import com.omarserrar.textme.services.responses.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/messenger")
public class MessengerController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessengerService messengerService;
    @GetMapping("conversation/init/{user}")
    public ResponseEntity getConversations(@PathVariable("user") Long id){
        try{
            User u = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException());
            Conversation c = messengerService.initConversation(u).orElseThrow(() -> new RuntimeException());
            return ResponseEntity.ok(c);
        }
        catch (UserNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ErrorResponse(e));
        }
        catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ErrorResponse(e));
        } catch (NotAuth e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e));
        }

    }
    @GetMapping("conversation")
    public ResponseEntity getConversationWithUserOrInit(){
        try{
            List<Conversation> conversations = messengerService.getUserConversation().orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
            return ResponseEntity.ok(conversations);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ErrorResponse(e));
        }

    }
    @GetMapping("conversation/user/{id}")
    public ResponseEntity getConversationWithUser(@PathVariable("id") Long userId){
        try {
            Conversation c = messengerService.getConversationWithUser(userId);
            return ResponseEntity.ok(c);
        } catch (ConversationNotFound e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
        } catch (NotAuth e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e));
        }

    }
    @GetMapping("conversation/{id}")
    public ResponseEntity getConversation(@PathVariable("id") Long id){
        try {
            Conversation c = messengerService.getConversation(id);

            return ResponseEntity.ok(c);
        } catch (ConversationNotFound e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
        } catch (ActionNotPermitted e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e));
        } catch (NotAuth e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e));
        }

    }
    @PostMapping("conversation/{id}")
    public ResponseEntity sendMessage(@PathVariable("id") Long id, @RequestBody MessageRequest message){
        try {
            Conversation c = messengerService.sendMessage(message, id);
            return ResponseEntity.ok(c.getMessages());
        } catch (NotAuth e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e));
        } catch (ActionNotPermitted | ConversationNotFound e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
        }
    }
}
