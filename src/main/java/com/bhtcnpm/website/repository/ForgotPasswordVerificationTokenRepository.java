package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteEntities.ForgotPasswordVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ForgotPasswordVerificationTokenRepository extends JpaRepository<ForgotPasswordVerificationToken, Long> {
    ForgotPasswordVerificationToken findByUserEmailAndTokenAndExpirationTimeAfter (String userEmail,
                                                                                String token,
                                                                                LocalDateTime now);

    ForgotPasswordVerificationToken findByUserNameAndTokenAndExpirationTimeAfter (String username,
                                                                               String token,
                                                                               LocalDateTime now);

    ForgotPasswordVerificationToken findByUser (UserWebsite user);
}
