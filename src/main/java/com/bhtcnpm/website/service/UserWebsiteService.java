package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserWebsite.*;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

public interface UserWebsiteService {
    UserAuthenticatedDTO createNewNormalUser(@Valid UserWebsiteCreateNewRequestDTO requestDTO);

    @Transactional(readOnly = true)
    UserAuthenticatedDTO loginUser (@Valid UserWebsiteLoginRequestDTO requestDTO);

    boolean forgotPassword (UserWebsiteForgotPasswordRequestDTO requestDTO);

    boolean resetPassword (UserWebsitePasswordResetRequestDTO pwResetDTO);

    boolean verifyEmailToken (String email, String verificationToken);
}
