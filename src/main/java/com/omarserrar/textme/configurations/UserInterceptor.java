package com.omarserrar.textme.configurations;

import com.omarserrar.textme.util.JWTUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;

public class UserInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        System.out.println("New Connection "+accessor.getCommand() );
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
                Object name = ((Map) raw).get("jwt");
                if (name instanceof ArrayList) {
                    accessor.setUser(new Principal() {
                        @Override
                        public String getName() {
                            String jwt = ((ArrayList<?>) name).get(0).toString();
                            System.out.println("New user : "+jwt);
                            return jwt;
                        }
                    });
                }
            }
        }
        return message;
    }
}
