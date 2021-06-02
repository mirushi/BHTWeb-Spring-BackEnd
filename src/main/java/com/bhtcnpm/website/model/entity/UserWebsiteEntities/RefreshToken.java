package com.bhtcnpm.website.model.entity.UserWebsiteEntities;

import com.bhtcnpm.website.constant.domain.UserWebsite.RTDomainConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @Column(
            name = "token",
            length = RTDomainConstant.REFRESH_TOKEN_LENGTH,
            updatable = false,
            unique = true,
            nullable = false
    )
    private String token;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            updatable = false,
            nullable = false
    )
    @Fetch(value = FetchMode.JOIN)
    private UserWebsite user;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken)) return false;
        RefreshToken other = (RefreshToken) o;
        return Objects.equals(getToken(), other.getToken());
    }

    @Override
    public int hashCode () {
        return Objects.hash(getToken());
    }
}
