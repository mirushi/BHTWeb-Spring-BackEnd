package com.bhtcnpm.website.model.entity.UserWebsiteEntities;

import com.bhtcnpm.website.constant.business.UserWebsite.EVTBusinessConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@FilterDef(name = "activeEVT")
@Filter(name = "activeEVT", condition = "expirationTime > ")
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
            columnDefinition = "BINARY(16)",
            nullable = false,
            unique = true,
            updatable = false
    )
    private UUID token;

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
        //Generate token.
        token = UUID.randomUUID();

        //Generate expirationTime.
        expirationTime = LocalDateTime.now().plusMinutes(EVTBusinessConstant.MAIL_VERIFY_TOKEN_EXPIRATION_TIME);
    }
}
