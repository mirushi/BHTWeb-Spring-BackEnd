package com.bhtcnpm.website.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
    
    @ManyToMany (
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            },
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<UserWebsiteAuthority> authorities;

    @Version
    private short version;
}
