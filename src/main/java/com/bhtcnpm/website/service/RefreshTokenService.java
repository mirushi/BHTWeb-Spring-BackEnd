package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteEntities.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createNewRefreshToken (UserWebsite userWebsite);
    RefreshToken getRefreshToken (String refreshToken);
}
