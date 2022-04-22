package com.omarserrar.textme.services;

import com.omarserrar.textme.models.messenger.Conversation;
import com.omarserrar.textme.models.messenger.Message;
import com.omarserrar.textme.models.messenger.MessageRepository;
import com.omarserrar.textme.models.messenger.MessengerRepository;
import com.omarserrar.textme.models.messenger.exceptions.ConversationNotFound;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.models.user.exceptions.ActionNotPermitted;
import com.omarserrar.textme.models.user.exceptions.NotAuth;
import com.omarserrar.textme.models.user.exceptions.UserNotFoundException;
import com.omarserrar.textme.services.requests.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessengerService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessengerRepository messengerRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    NotificationService notificationService;
    public Optional<List<Conversation>> getUserConversation(){
        try{
            User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(connectedUser != null){
                List<Conversation> list = messengerRepository.getUserConversation(connectedUser.getId());
                return Optional.of(list);
            }
        }
        catch (Exception e){
            return Optional.empty();
        }
        return Optional.empty();
    }
    public Conversation getConversation(Long conversationId) throws NotAuth, ActionNotPermitted, ConversationNotFound {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(connectedUser == null) throw new NotAuth();
        Conversation c = messengerRepository.findById(conversationId).orElseThrow(()->new ConversationNotFound());
        if(c.getUser2().getId() != connectedUser.getId() && c.getUser1().getId() != connectedUser.getId())
            throw new ActionNotPermitted("You are not permitted to access this conversation");
        return c;
    }
    public Conversation getConversationWithUser(Long userId) throws NotAuth, UserNotFoundException, ConversationNotFound {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(connectedUser == null) throw new NotAuth();
        User user2 = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        return getConversation(connectedUser, user2).orElseThrow(() -> new ConversationNotFound());
    }
    public Optional<Conversation> getConversation(User u1, User u2){
        return Optional.ofNullable(this.messengerRepository.getConversationFromUsers(u1.getId(), u2.getId()));
    }
    public Optional<Conversation> initConversation(User user) throws UserNotFoundException, NotAuth {
        try{
            User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(connectedUser == null) throw new NotAuth();
            if(user == null) throw new UserNotFoundException();
            Conversation c;
            c = this.messengerRepository.getConversationFromUsers(user.getId(), connectedUser.getId());
            if(c==null){
                c = Conversation.builder().user2(user).user1(connectedUser).build();
                c = messengerRepository.save(c);
            }
            return Optional.of(c);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    public Conversation sendMessage(MessageRequest msg, Long conversationId) throws NotAuth, ActionNotPermitted, ConversationNotFound {
        Conversation conversation = messengerRepository.findById(conversationId).orElseThrow(() -> new ConversationNotFound());
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(connectedUser == null) throw new NotAuth();
        Message m = Message.builder().sender(connectedUser).textContent(msg.getMessage()).sentDate(new Timestamp(new Date().getTime())).build();
        return sendMessage(conversation, connectedUser, m);
    }
    public Conversation sendMessage(Conversation c, User u, Message m) throws ActionNotPermitted {
        if(c.getUser1().getId() == u.getId() || c.getUser2().getId() == u.getId() ){
            messageRepository.save(m);
            c.getMessages().add(m);
            messengerRepository.save(c);
            User dest = (c.getUser1().getId() == u.getId())?c.getUser2():c.getUser1();
            notificationService.sendMessageNotification(dest,m);
            return c;
        }
        else {
            throw new ActionNotPermitted("You can't send message in this conversation");
        }
    }
}
