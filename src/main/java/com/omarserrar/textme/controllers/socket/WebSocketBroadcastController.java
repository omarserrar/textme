package com.omarserrar.textme.controllers.socket;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.function.ServerRequest;

@Controller
public class WebSocketBroadcastController {


    @MessageMapping("/typing")
    public Long send(Message message, Long conversationId) throws Exception {
        System.out.println();
        System.out.println("ID "+conversationId);
        System.out.println(message);
        System.out.println();
        return conversationId;
    }
}