package com.omarserrar.textme.models.requests;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Data

public class UserEditRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public boolean validate(){
        return username==null || (username !=null && username.trim().length()<=20 && username.trim().length()>=3 )&&
                firstName==null || (firstName !=null && firstName.trim().length()<=20 && firstName.trim().length()>=3 )&&
                lastName==null || (lastName !=null && lastName.trim().length()<=20 && lastName.trim().length()>=3);
    }
}
