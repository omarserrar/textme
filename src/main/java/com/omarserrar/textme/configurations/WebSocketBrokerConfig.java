package com.omarserrar.textme.configurations;

import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.services.NotificationService;
import com.omarserrar.textme.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setUserDestinationPrefix("/user");
        registry.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").setAllowedOrigins("*");
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(userInterceptor);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration){

        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                System.out.println("Sending "+message);
                return ChannelInterceptor.super.preSend(message, channel);
            }

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
                System.out.println("Sending "+message);
                ChannelInterceptor.super.postSend(message, channel, sent);
            }
        });
    }

}
