package com.omarserrar.textme.controllers.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class WebSocketBroadcastController {

    @GetMapping("/stomp-broadcast")
    public String getWebSocketBroadcast() {
        return "stomp-broadcast";
    }

    @MessageMapping("/broadcast")
    @SendTo("/topic/messages")
    public String send(String chatMessage) throws Exception {
        return chatMessage;
    }
}