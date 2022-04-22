package com.omarserrar.textme.models.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;


@Entity
@Table(name = "picture")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Image {
    @Id
    @SequenceGenerator(
            name = "picture_id_sequence",
            sequenceName = "picture_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "picture_id_sequence"
    )
    private long id;

    private String type;

    private String fileName;

    @Fetch(FetchMode.SELECT)
    @Lob
    private byte[] data;
}
