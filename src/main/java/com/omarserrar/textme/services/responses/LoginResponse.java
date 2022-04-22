package com.omarserrar.textme.services.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
   private String JWT;
   private boolean error ;
   private String message;
}
