package com.omarserrar.textme.models.requests;

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
}
