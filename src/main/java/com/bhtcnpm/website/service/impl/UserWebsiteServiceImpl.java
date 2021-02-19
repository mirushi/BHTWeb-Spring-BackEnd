package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.domain.UserWebsiteRole.UWRRequiredRole;
import com.bhtcnpm.website.model.dto.UserWebsite.UserWebsiteCreateNewRequestDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserWebsiteCreateNewRequestMapper;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteRole;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.repository.UserWebsiteRoleRepository;
import com.bhtcnpm.website.security.JwtTokenProvider;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWebsiteServiceImpl implements UserWebsiteService {

    private final UserWebsiteRepository uwRepository;
    private final UserWebsiteRoleRepository uwRoleRepository;
    private final UserWebsiteCreateNewRequestMapper uwCreateRequestMapper;

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserWebsite createNewNormalUser(@Valid UserWebsiteCreateNewRequestDTO requestDTO) {
        UserWebsiteRole normalUserRole = uwRoleRepository.getOne(UWRRequiredRole.USER_ROLE_ID);
        UserWebsite userWebsite = uwCreateRequestMapper
                .userWebsiteCreateNewRequestToUserWebsite(requestDTO, new HashSet<UserWebsiteRole>(Arrays.asList(normalUserRole)));

        userWebsite = uwRepository.save(userWebsite);
        return null;
    }

    private void authenticateUser (String username, String normalPassword) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, normalPassword));
    }

}
