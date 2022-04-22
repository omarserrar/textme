package com.omarserrar.textme.models.messenger;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.omarserrar.textme.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity()
@Table(name = "Message")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Message {
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
    private String textContent;
    @OneToOne
    private User sender;

    private Timestamp sentDate;
}
