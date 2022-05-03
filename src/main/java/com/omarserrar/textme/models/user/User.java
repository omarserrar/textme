package com.omarserrar.textme.models.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.omarserrar.textme.models.messenger.Conversation;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity()
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )

    private long id;

    private String firstName;

    private String lastName;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Date birthDate;

    private String email;

    private String phoneNumber;

    @ManyToMany()
    @Fetch(FetchMode.JOIN)
    private Set<User> contacts;

    @OneToMany()
    @Fetch(FetchMode.JOIN)
    private Set<UserSessions> sessions;

    @JsonIgnore
    @OneToOne
    @Fetch(FetchMode.SELECT)
    private Image userPicture;

    private Boolean online = false;
    private Timestamp lastOnline;

    private Timestamp creationDate;


    private Boolean guest = false;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setOnline(){
        online = true;
        lastOnline = new Timestamp(System.currentTimeMillis());
    }
    public void setOffline(){
        online = false;
        lastOnline = new Timestamp(System.currentTimeMillis());
    }
}
