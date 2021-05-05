package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.domain.UserWebsiteRole.UWRRequiredRole;
import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteEntities.EmailVerificationToken;
import com.bhtcnpm.website.model.entity.UserWebsiteEntities.ForgotPasswordVerificationToken;
import com.bhtcnpm.website.model.entity.UserWebsiteRole;
import com.bhtcnpm.website.model.exception.CaptchaInvalidException;
import com.bhtcnpm.website.model.exception.CaptchaServerErrorException;
import com.bhtcnpm.website.repository.EmailVerificationTokenRepository;
import com.bhtcnpm.website.repository.ForgotPasswordVerificationTokenRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.repository.UserWebsiteRoleRepository;
import com.bhtcnpm.website.security.JwtTokenProvider;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.CaptchaService;
import com.bhtcnpm.website.service.EmailService;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final EmailService emailService;

    private final CaptchaService captchaService;

    @Override
    public UserAuthenticatedDTO createNewNormalUser(@Valid UserWebsiteCreateNewRequestDTO requestDTO)
            throws CaptchaServerErrorException, CaptchaInvalidException {
        //TODO: Get user IP address.
        boolean captchaValid = captchaService.verifyCaptcha(requestDTO.getCaptcha(), null);
        if (!captchaValid) {
            return null;
        }

        UserWebsiteRole notVerifiedRole = uwRoleRepository.getOne(UWRRequiredRole.EMAIL_NOT_VERIFIED_ROLE_ID);
        UserWebsite userWebsite = uwCreateRequestMapper
                .userWebsiteCreateNewRequestToUserWebsite(requestDTO, new HashSet<>(Collections.singletonList(notVerifiedRole)));

        userWebsite = uwRepository.save(userWebsite);
        HttpHeaders headers = SecurityUtils.getJwtHeader(userWebsite, jwtTokenProvider);

        afterCreatedUser(userWebsite);

        return userAuthenticatedMapper.userWebsiteToUserAuthenticatedDTO(userWebsite, headers);
    }

    private void afterCreatedUser (UserWebsite userWebsite) {
        //Generate verification token.
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setUser(userWebsite);
        emailVerificationToken = emailVerificationTokenRepository.save(emailVerificationToken);

        //Send verification token to user.
        emailService.sendConfirmationEmail(userWebsite.getEmail(), emailVerificationToken.getToken());
    }

    @Override
    public UserAuthenticatedDTO loginUser(@Valid UserWebsiteLoginRequestDTO requestDTO)
            throws CaptchaServerErrorException, CaptchaInvalidException {
        //TODO: Get user IP address.
        boolean captchaValid = captchaService.verifyCaptcha(requestDTO.getCaptcha(), null);
        if (!captchaValid) {
            return null;
        }

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
    public boolean forgotPassword(UserWebsiteForgotPasswordRequestDTO requestDTO) throws
            CaptchaServerErrorException, CaptchaInvalidException {
        //First and foremost, we must verify whenever CAPTCHA token provided is valid.
        //TODO: Get user IP address.
        boolean captchaValid = captchaService.verifyCaptcha(requestDTO.getCaptcha(), null);
        if (!captchaValid) {
            return false;
        }
        //Remember: Never let client know if email/username exist when forgot password.
        //Only return false when there's error in making forgot password request.

        Optional<UserWebsite> user = uwRepository.findByNameOrDisplayNameOrEmail(requestDTO.getUsername(), null, requestDTO.getEmail());

        //Never let client know if email/username exists.
        if (!user.isPresent()) {
            return true;
        }

        //We only allow password reset if account is not 'EMAIL_NOT_VERIFIED' (aka only verified account can do forgot password).
        //Because if not, we risk sending multiple forgot password emails to invalid email address.
        //TODO: We may improve in the future to support for permission checking instead of role checking.
        if (user.get().getRoles()
                .stream()
                .map(UserWebsiteRole::getId)
                .collect(Collectors.toList()).contains(UWRRequiredRole.EMAIL_NOT_VERIFIED_ROLE_ID)) {
            throw new IllegalArgumentException("Your email account is not email-verified.");
        }

        ForgotPasswordVerificationToken forgotPassToken = forgotPasswordVerificationTokenRepository.findByUser(user.get());

        //If current token is still active, we'll expire it before assigning a new one.
        if (forgotPassToken != null) {
            forgotPassToken.setExpirationTime(LocalDateTime.now());
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
    public boolean resetPassword(UserWebsitePasswordResetRequestDTO pwResetDTO) {
        //First we have to make sure that the token is valid.
        ForgotPasswordVerificationToken token =
                forgotPasswordVerificationTokenRepository.findByUserEmailAndTokenAndExpirationTimeAfter(
                        pwResetDTO.getEmail(), pwResetDTO.getToken(), LocalDateTime.now()
                );

        //Token which user provided is invalid.
        if (token == null) {
            return false;
        }

        //Token is valid, we'll reset the password accordingly to the provided one.
        String newEncodedPassword = SecurityUtils.getEncodedPassword(pwResetDTO.getPassword(), passwordEncoder);

        UserWebsite user = token.getUser();
        user.setHashedPassword(newEncodedPassword);
        uwRepository.save(user);

        //Invalidate token.
        token.setExpirationTime(LocalDateTime.now());

        return true;
    }

    @Override
    public boolean verifyEmailToken(String email, String verificationToken) {
        EmailVerificationToken tokenEntity = emailVerificationTokenRepository
                .findByUserEmailAndTokenAndExpirationTimeAfter(email,
                        verificationToken, LocalDateTime.now());

        if (tokenEntity == null) {
            throw new IllegalArgumentException("Your email & token combination is invalid.");
        }

        tokenEntity.setExpirationTime(LocalDateTime.now());
        emailVerificationTokenRepository.save(tokenEntity);

        UserWebsiteRole emailNotVerifiedRole = new UserWebsiteRole();
        emailNotVerifiedRole.setName(UWRRequiredRole.EMAIL_NOT_VERIFIED_ROLE_NAME);

        UserWebsite user = tokenEntity.getUser();
        user.removeRole(emailNotVerifiedRole);
        user.addRole(uwRoleRepository.getOne(UWRRequiredRole.USER_ROLE_ID));
        uwRepository.save(user);

        return true;
    }

    @Override
    public List<String> checkUserExists (String name, String displayName, String email) {
        List<UserWebsite> userList = uwRepository.findAllByNameOrDisplayNameOrEmail(name, displayName, email);
        boolean isNameExist = false;
        boolean isDisplayNameExist = false;
        boolean isEmailExist = false;

        List<String> existedFields = new ArrayList<>();

        for (UserWebsite user : userList) {
            if (user.getName().equals(name)) {
                isNameExist = true;
            }
            if (user.getDisplayName().equals(displayName)) {
                isDisplayNameExist = true;
            }
            if (user.getEmail().equals(email)) {
                isEmailExist = true;
            }
        }

        if (isNameExist) {
            existedFields.add("name");
        }
        if (isDisplayNameExist) {
            existedFields.add("displayName");
        }
        if (isEmailExist) {
            existedFields.add("email");
        }

        return existedFields;
    }
}
