package com.omarserrar.textme.models.messenger;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.omarserrar.textme.models.user.Image;
import com.omarserrar.textme.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    @OneToOne(fetch = FetchType.EAGER)
    private Image image;

    private Timestamp sentDate;

    private Timestamp seen;
}
