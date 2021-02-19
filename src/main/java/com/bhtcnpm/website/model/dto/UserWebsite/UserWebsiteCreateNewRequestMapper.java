package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteRole;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Mapper
public abstract class UserWebsiteCreateNewRequestMapper {

    public static final UserWebsiteCreateNewRequestMapper INSTANCE = Mappers.getMapper(UserWebsiteCreateNewRequestMapper.class);

    private PasswordEncoder passwordEncoder;

    //TODO: Change default value to custom BHTCNPM default avatar image.
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
                .hashedPassword(passwordEncoder.encode(createRequestDTO.getPassword()))
                .avatarURL(avatarURL)
                .reputationScore(1L)
                .roles(userWebsiteRoles)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .banStatus(false).build();

        return userWebsite;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
