package com.bhtcnpm.website.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user_website_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWebsiteRole implements Serializable {

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

    @Column(nullable = false, unique = true)
    @NaturalId
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

    public UserWebsiteRole (Long id) {
        this.id = id;
    }
    
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserWebsiteRole)) return false;
        UserWebsiteRole other = (UserWebsiteRole)o;
        return Objects.equals(getName(), other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
