package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.config.SecurityConfig;
import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteRole;
import com.bhtcnpm.website.security.util.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Mapper
public abstract class UserWebsiteRequestMapper {

    public static final UserWebsiteRequestMapper INSTANCE = Mappers.getMapper(UserWebsiteRequestMapper.class);

    public UserWebsite userWebsiteCreateNewRequestToUserWebsite(UserWebsiteCreateNewRequestDTO createRequestDTO, Set<UserWebsiteRole> userWebsiteRoles) {
        if (createRequestDTO == null) {
            return null;
        }

        //Default value initialization.
        String avatarURL = createRequestDTO.getAvatarURL();
        if (StringUtils.isBlank(avatarURL)) {
            avatarURL = UWBusinessConstant.DEFAULT_AVATAR_URL;
        }

        UserWebsite userWebsite = UserWebsite.builder()
                .id(null)
                .name(createRequestDTO.getName())
                .displayName(createRequestDTO.getDisplayName())
                .email(createRequestDTO.getEmail())
                .avatarURL(avatarURL)
                .reputationScore(UWBusinessConstant.DEFAULT_REPUTATION_SCORE)
                .roles(userWebsiteRoles)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true).build();

        return userWebsite;
    }
}
