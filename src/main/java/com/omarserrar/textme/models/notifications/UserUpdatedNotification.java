package com.omarserrar.textme.models.notifications;

import com.omarserrar.textme.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserUpdatedNotification extends Notification{
    User u;
    String message;
    public UserUpdatedNotification(){
        super("USER_UPDATE");
    }
}
