package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserWebsite.UserAuthenticatedDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserWebsiteCreateNewRequestDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserWebsiteLoginRequestDTO;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

public interface UserWebsiteService {
    UserAuthenticatedDTO createNewNormalUser(@Valid UserWebsiteCreateNewRequestDTO requestDTO);

    @Transactional(readOnly = true)
    UserAuthenticatedDTO loginUser (@Valid UserWebsiteLoginRequestDTO requestDTO);
}
