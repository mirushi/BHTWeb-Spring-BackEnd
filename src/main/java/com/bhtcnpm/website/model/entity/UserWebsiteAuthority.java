package com.bhtcnpm.website.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_website_authority")
@Data
public class UserWebsiteAuthority {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_website_authority_sequence"
    )
    @SequenceGenerator(
            name = "user_website_authority_sequence",
            sequenceName = "user_website_authority_sequence",
            allocationSize = 3
    )
    private Long id;

    @Column(nullable = false)
    private String permission;
}
