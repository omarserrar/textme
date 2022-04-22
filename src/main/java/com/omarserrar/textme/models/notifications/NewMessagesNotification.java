package com.omarserrar.textme.models.notifications;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor

public class NewMessagesNotification extends Notification{
    private String body = null;
    NewMessagesNotification(){
        super("NEW_MESSAGES");
    }
}
