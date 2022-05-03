package com.omarserrar.textme.models.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.security.Principal;
import java.sql.Timestamp;

@Entity()
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserSessions implements Principal {
    @Id
    String jwt;


    @Override
    public String getName() {
        return jwt;
    }

}
