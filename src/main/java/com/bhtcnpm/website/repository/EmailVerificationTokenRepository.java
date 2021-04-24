package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.UserWebsiteEntities.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    EmailVerificationToken findByUserEmailAndTokenAndExpirationTimeAfter (String userEmail,
                                                                           String token,
                                                                           LocalDateTime now);
}
