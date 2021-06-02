package com.bhtcnpm.website.model.entity.UserWebsiteEntities;

import com.bhtcnpm.website.constant.business.UserWebsite.VTBusinessConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Entity
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
            unique = true,
            nullable = false,
            updatable = false
    )
    private String token;

    @OneToOne
    @JoinColumn(
            name = "user_id",
            unique = true,
            nullable = false,
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
        token = new BigInteger(
                VTBusinessConstant.MAIL_VERIFY_TOKEN_NUM_CHAR * VTBusinessConstant.BIT_PER_CHAR,
                random
        ).toString(32);

        //Generate expirationTime.
        expirationTime = LocalDateTime.now().plusMinutes(VTBusinessConstant.MAIL_VERIFY_TOKEN_EXPIRATION_TIME);
    }
}
