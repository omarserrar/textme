package com.omarserrar.textme.services;

import com.omarserrar.textme.models.messenger.Conversation;
import com.omarserrar.textme.models.messenger.Message;
import com.omarserrar.textme.models.notifications.NewMessagesNotification;
import com.omarserrar.textme.models.notifications.Notification;
import com.omarserrar.textme.models.notifications.SeenNotification;
import com.omarserrar.textme.models.notifications.UserUpdatedNotification;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.models.user.UserSessions;
import com.omarserrar.textme.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private SimpUserRegistry simpUserRegistry;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private UserService userService;

    public NewMessagesNotification sendMessageNotification(User user, Message message){
        NewMessagesNotification notification = NewMessagesNotification.builder().body("You have new messages").build();
        System.out.println("\nSending notification to "+user.getUsername());
        sendNotificationToUser(user, "/topic/notification/message", notification);
        /*user.getSessions().stream().forEach((userSession) ->{
            try{
                System.out.println("Session: "+userSession.getJwt());
                messagingTemplate.convertAndSendToUser(userSession.getJwt(),"/topic/notification/message", notification);
            }
            catch (Exception e){
                System.err.println("Could not send notification");
            }
        });*/

        return notification;
    }
    public SeenNotification seen(User user, Conversation c, Message m){
        SeenNotification notification = SeenNotification.builder().cid(c.getId()).mid(m.getId()).body("Your message has been seen.").build();
        System.out.println("\nSending seen notification to "+user.getUsername());
        sendNotificationToUser(user, "/topic/notification/seen", notification);
       /* user.getSessions().stream().forEach((userSession) ->{
            try{
                System.out.println("Session: "+userSession.getJwt());
                messagingTemplate.convertAndSendToUser(userSession.getJwt(),"/topic/notification/seen", notification);
            }
            catch (Exception e){
                System.err.println("Could not send notification");
            }
        });*/

        return notification;
    }
    public void sendNotificationToUser(User user,String topic, Notification notification){
        simpUserRegistry.getUsers().stream()
                .filter((u)->{
                    String jwt = u.getName();
                    return !JWTUtils.isExpired(jwt) && JWTUtils.getUserId(jwt)==user.getId();
                })
                .forEach((u)->{
                    System.out.println("Sending to "+user.getUsername()+" "+topic);
                    messagingTemplate.convertAndSendToUser(u.getName(),topic, notification);
                });
    }
    public void sendUserOnlineToAll(User u){
        messagingTemplate.convertAndSend("/topic/update", UserUpdatedNotification.builder().u(u).message("Is now Online").build());
    }
    public void sendUserOfflineToAll(User u){
        messagingTemplate.convertAndSend("/topic/update", UserUpdatedNotification.builder().u(u).message("Is now Offline").build());
    }
    public void sendUserUpdateToAll(User u){
        messagingTemplate.convertAndSend("/topic/update", UserUpdatedNotification.builder().u(u).message("User updated").build());
    }
}
