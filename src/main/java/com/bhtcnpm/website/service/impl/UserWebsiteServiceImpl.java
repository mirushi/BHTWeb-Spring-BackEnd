package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.config.SecurityConfig;
import com.bhtcnpm.website.constant.domain.UserWebsiteRole.UWRRequiredRole;
import com.bhtcnpm.website.constant.security.SecurityConstant;
import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteRole;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.repository.UserWebsiteRoleRepository;
import com.bhtcnpm.website.security.JwtTokenProvider;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class UserWebsiteServiceImpl implements UserWebsiteService {

    private final UserWebsiteRepository uwRepository;
    private final UserWebsiteRoleRepository uwRoleRepository;
    private final UserWebsiteRequestMapper uwCreateRequestMapper;
    private final UserAuthenticatedMapper userAuthenticatedMapper;

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserAuthenticatedDTO createNewNormalUser(@Valid UserWebsiteCreateNewRequestDTO requestDTO) {
        UserWebsiteRole normalUserRole = uwRoleRepository.getOne(UWRRequiredRole.USER_ROLE_ID);
        UserWebsite userWebsite = uwCreateRequestMapper
                .userWebsiteCreateNewRequestToUserWebsite(requestDTO, new HashSet<>(Collections.singletonList(normalUserRole)));

        userWebsite = uwRepository.save(userWebsite);
        HttpHeaders headers = getJwtHeader(userWebsite);

        return userAuthenticatedMapper.userWebsiteToUserAuthenticatedDTO(userWebsite, headers);
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

        authenticateUser(username, password);

        HttpHeaders headers = getJwtHeader(userWebsite.get());

        return userAuthenticatedMapper.userWebsiteToUserAuthenticatedDTO(userWebsite.get(), headers);
    }

    private void authenticateUser (String username, String normalPassword) {
        String prefixedPassword = StringUtils.prependIfMissingIgnoreCase(normalPassword, SecurityConfig.CURRENT_ENCODING_ALGO);
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, prefixedPassword));
    }

    private HttpHeaders getJwtHeader (UserWebsite userWebsite) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userWebsite));

        return headers;
    }
}
