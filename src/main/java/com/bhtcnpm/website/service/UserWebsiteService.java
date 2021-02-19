package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserWebsite.UserWebsiteCreateNewRequestDTO;
import com.bhtcnpm.website.model.entity.UserWebsite;

public interface UserWebsiteService {
    UserWebsite createNewNormalUser(UserWebsiteCreateNewRequestDTO requestDTO);

}
