package com.bhtcnpm.website.model.entity.UserWebsiteEntities;

import com.bhtcnpm.website.constant.business.UserWebsite.EVTBusinessConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Loader;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Loader(namedQuery = "loadActiveToken")
@NamedQuery(
        name = "loadActiveToken",
        query = "SELECT evt FROM EmailVerificationToken evt WHERE evt.expirationTime > current_timestamp"
)
@Getter
@Setter
public class EmailVerificationToken {
    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "email_verification_sequence"
    )
    @SequenceGenerator(
            name = "email_verification_sequence",
            sequenceName = "email_verification_sequence",
            allocationSize = 50
    )
    private Long id;

    @Column(
            name = "token",
            nullable = false,
            unique = true,
            updatable = false
    )
    private String token;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "user_id",
            updatable = false
    )
    private UserWebsite user;

    @Column(name = "expirationTime")
    private LocalDateTime expirationTime;

    @PrePersist
    public void prePersist () {
        //Secure random.
        SecureRandom random = new SecureRandom();

        //Generate token.
        token = new BigInteger(260, random).toString(32);

        //Generate expirationTime.
        expirationTime = LocalDateTime.now().plusMinutes(EVTBusinessConstant.MAIL_VERIFY_TOKEN_EXPIRATION_TIME);
    }
}
