package com.omarserrar.textme.models.responses;

import com.omarserrar.textme.models.user.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterResponse {
    User u;
    String jwt;
}
