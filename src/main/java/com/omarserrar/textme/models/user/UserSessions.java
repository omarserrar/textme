package com.omarserrar.textme.models.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity()
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserSessions {
    @Id
    String jwt;

}
