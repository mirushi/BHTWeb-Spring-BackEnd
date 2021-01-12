package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_website_role")
@Data
public class UserWebsiteRole {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "user_website_role_sequence"
    )
    @SequenceGenerator(
            name = "user_website_role_sequence",
            sequenceName = "user_website_role_sequence",
            allocationSize = 3
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany (
            mappedBy = "role",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    private Set<UserWebsite> users;

    @Version
    private short version;
}
