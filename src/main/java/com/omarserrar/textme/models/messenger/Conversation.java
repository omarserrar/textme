package com.omarserrar.textme.models.messenger;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.omarserrar.textme.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;


@Entity()
@Table(name = "Conversation")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Conversation {
    @Id
    @SequenceGenerator(
            name = "message_id_sequence",
            sequenceName = "message_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "message_id_sequence"
    )
    private long id;
    @OneToOne
    User user1;
    @OneToOne
    User user2;

    @OneToMany()
    @Fetch(FetchMode.JOIN)
    @OrderBy("sentDate ASC")
    private Set<Message> messages;
}
