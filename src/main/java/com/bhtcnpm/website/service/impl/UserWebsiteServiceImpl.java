package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.config.email.EmailMessage;
import com.bhtcnpm.website.constant.domain.UserWebsiteRole.UWRRequiredRole;
import com.bhtcnpm.website.model.dto.EmailTemplate;
import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteRole;
import com.bhtcnpm.website.repository.EmailVerificationTokenRepository;
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
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
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
        

        //Send verification token to user.
        emailService.sendConfirmationEmail(userWebsite.getEmail());
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
    public boolean verifyEmailToken(String email, String verificationToken) {
        boolean isVerificationValid = emailVerificationTokenRepository
                .existsByUserEmailAndToken(email, verificationToken);
        return false;
    }
}
