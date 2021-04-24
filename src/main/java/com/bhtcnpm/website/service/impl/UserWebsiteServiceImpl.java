package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.domain.UserWebsiteRole.UWRRequiredRole;
import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteEntities.EmailVerificationToken;
import com.bhtcnpm.website.model.entity.UserWebsiteEntities.ForgotPasswordVerificationToken;
import com.bhtcnpm.website.model.entity.UserWebsiteRole;
import com.bhtcnpm.website.repository.EmailVerificationTokenRepository;
import com.bhtcnpm.website.repository.ForgotPasswordVerificationTokenRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.repository.UserWebsiteRoleRepository;
import com.bhtcnpm.website.security.JwtTokenProvider;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.EmailService;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
@Slf4j
public class UserWebsiteServiceImpl implements UserWebsiteService {

    private final UserWebsiteRepository uwRepository;
    private final UserWebsiteRoleRepository uwRoleRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final ForgotPasswordVerificationTokenRepository forgotPasswordVerificationTokenRepository;
    private final UserWebsiteRequestMapper uwCreateRequestMapper;
    private final UserAuthenticatedMapper userAuthenticatedMapper;

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final EmailService emailService;

    @Override
    public UserAuthenticatedDTO createNewNormalUser(@Valid UserWebsiteCreateNewRequestDTO requestDTO) {
        UserWebsiteRole normalUserRole = uwRoleRepository.getOne(UWRRequiredRole.USER_ROLE_ID);
        UserWebsite userWebsite = uwCreateRequestMapper
                .userWebsiteCreateNewRequestToUserWebsite(requestDTO, new HashSet<>(Collections.singletonList(normalUserRole)));

        userWebsite = uwRepository.save(userWebsite);
        HttpHeaders headers = SecurityUtils.getJwtHeader(userWebsite, jwtTokenProvider);

        afterCreatedUser(userWebsite);

        return userAuthenticatedMapper.userWebsiteToUserAuthenticatedDTO(userWebsite, headers);
    }

    private void afterCreatedUser (UserWebsite userWebsite) {
        //Generate verification token.
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setUser(userWebsite);
        emailVerificationTokenRepository.save(emailVerificationToken);

        //Send verification token to user.
        emailService.sendConfirmationEmail(userWebsite.getEmail(), emailVerificationToken.getToken());
    }

    @Override
    public UserAuthenticatedDTO loginUser(@Valid UserWebsiteLoginRequestDTO requestDTO) {
        //Because of constraint validation, we can be sure that only username or email is present. Not both, nor both won't exist.
        String username = requestDTO.getUsername();
        String password = requestDTO.getPassword();

        if (StringUtils.isBlank(username) && StringUtils.isNotBlank(requestDTO.getEmail())) {
            username = uwRepository.findUsernameByEmail(requestDTO.getEmail());
        }

        Optional<UserWebsite> userWebsite = uwRepository.findByName(username);

        if (!userWebsite.isPresent()) {
            throw new ConstraintViolationException("Login credentials is invalid.", null);
        }

        SecurityUtils.authenticateUser(username, password, authManager);

        HttpHeaders headers = SecurityUtils.getJwtHeader(userWebsite.get(), jwtTokenProvider);

        return userAuthenticatedMapper.userWebsiteToUserAuthenticatedDTO(userWebsite.get(), headers);
    }

    @Override
    public boolean forgotPassword(UserWebsiteForgotPasswordRequestDTO requestDTO) {
        //Remember: Never let client know if email/username exist when forgot password.
        //TODO: We only allow password reset if account is activated.
        Optional<UserWebsite> user = uwRepository.findByNameOrDisplayNameOrEmail(requestDTO.getUsername(), null, requestDTO.getEmail());

        if (!user.isPresent()) {
            return true;
        }

        ForgotPasswordVerificationToken forgotPassToken = forgotPasswordVerificationTokenRepository.findByUser(user.get());
        if (forgotPassToken != null) {
            forgotPasswordVerificationTokenRepository.delete(forgotPassToken);
        }

        forgotPassToken = new ForgotPasswordVerificationToken();

        forgotPassToken.setUser(user.get());

        forgotPassToken = forgotPasswordVerificationTokenRepository.save(forgotPassToken);

        emailService.sendForgotPasswordEmail(
                user.get().getEmail(),
                forgotPassToken.getToken()
        );

        return true;
    }

    @Override
    public boolean verifyEmailToken(String email, String verificationToken) {
        EmailVerificationToken tokenEntity = emailVerificationTokenRepository
                .findByUserEmailAndTokenAndExpirationTimeAfter(email,
                        verificationToken, LocalDateTime.now());

        if (tokenEntity != null) {
            tokenEntity.setExpirationTime(LocalDateTime.now());
            emailVerificationTokenRepository.save(tokenEntity);
            return true;
        }

        return false;
    }
}
