package com.bhtcnpm.website.constant.business.UserWebsite;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UWBusinessConstant {
    public static final String DEFAULT_AVATAR_URL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/resources/images/user/default_avatar.png").toUriString();
}
