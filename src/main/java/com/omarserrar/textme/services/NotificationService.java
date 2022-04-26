package com.omarserrar.textme.services;

import com.omarserrar.textme.models.messenger.Conversation;
import com.omarserrar.textme.models.messenger.Message;
import com.omarserrar.textme.models.notifications.NewMessagesNotification;
import com.omarserrar.textme.models.notifications.SeenNotification;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public NewMessagesNotification sendMessageNotification(User user, Message message){
        NewMessagesNotification notification = NewMessagesNotification.builder().body("You have new messages").build();
        System.out.println("\nSending notification to "+user.getUsername());
        user.getSessions().stream().forEach((userSession) ->{
            try{
                System.out.println("Session: "+userSession.getJwt());
                messagingTemplate.convertAndSendToUser(userSession.getJwt(),"/topic/notification/message", notification);
            }
            catch (Exception e){
                System.err.println("Could not send notification");
            }
        });

        return notification;
    }
    public SeenNotification seen(User user, Conversation c, Message m){
        SeenNotification notification = SeenNotification.builder().cid(c.getId()).mid(m.getId()).body("Your message has been seen.").build();
        System.out.println("\nSending seen notification to "+user.getUsername());
        user.getSessions().stream().forEach((userSession) ->{
            try{
                System.out.println("Session: "+userSession.getJwt());
                messagingTemplate.convertAndSendToUser(userSession.getJwt(),"/topic/notification/seen", notification);
            }
            catch (Exception e){
                System.err.println("Could not send notification");
            }
        });

        return notification;
    }
}
