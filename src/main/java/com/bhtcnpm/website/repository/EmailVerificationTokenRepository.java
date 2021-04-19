package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.UserWebsiteEntities.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    EmailVerificationToken findByUserEmailAndToken (String userEmail, String token);
}
