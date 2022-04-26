package com.omarserrar.textme.models.notifications;

import com.omarserrar.textme.models.messenger.Conversation;
import com.omarserrar.textme.models.messenger.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class SeenNotification extends Notification{
    private String body = null;
    private Long cid;
    private Long mid;
    SeenNotification(){
        super("SEEN");
    }
}
