package com.omarserrar.textme.user.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Date birthDate;
    private String email;
    private String phoneNumber;
}
