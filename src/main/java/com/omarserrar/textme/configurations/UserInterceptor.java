package com.omarserrar.textme.configurations;

import com.omarserrar.textme.models.user.SessionRepository;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.models.user.UserSessions;
import com.omarserrar.textme.services.NotificationService;
import com.omarserrar.textme.services.UserService;
import com.omarserrar.textme.util.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

@Component
public class UserInterceptor implements ChannelInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Lazy
    @Autowired
    private NotificationService notificationService;
    String getJWT(Message message){
        Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

        if (raw instanceof Map) {
            Object name = ((Map) raw).get("jwt");
            //System.out.println("name "+name);
            if (name instanceof ArrayList) {
                String jwt = (String) ((ArrayList<?>) name).get(0);
                return jwt;
            }
        }
        Object simpSession = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER);
        if(simpSession instanceof UserSessions){
            String jwt = ((UserSessions) simpSession).getJwt();
            return jwt;
        }
        return null;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        //System.out.println("New Connection "+accessor.getCommand() );
        System.out.println("User Service "+userService==null);
        System.out.println("Message "+message);
        String jwt = getJWT(message);
        System.out.println("jwt "+jwt);
        //if(jwt==null) return message;

        User u = userService.getUserFromJWT(jwt);
        UserSessions s = new UserSessions(jwt);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            accessor.setUser(s);
        }

        if(StompCommand.DISCONNECT.equals(accessor.getCommand()) && u != null){
            u.setOffline();
            notificationService.sendUserOfflineToAll(u);
        }
        else if(u!=null){
            System.out.println("User online");
            u.setOnline();
            notificationService.sendUserOnlineToAll(u);
        }
        if(u != null){
            userRepository.save(u);

        }
        return message;
    }
}
