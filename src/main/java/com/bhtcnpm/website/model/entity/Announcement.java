package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcement")
@Data
public class Announcement {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "announcement_sequence"
    )
    @SequenceGenerator(
            name = "announcement_sequence",
            sequenceName = "announcement_sequence",
            allocationSize = 3
    )
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean activatedStatus;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @Version
    private short version;

}
