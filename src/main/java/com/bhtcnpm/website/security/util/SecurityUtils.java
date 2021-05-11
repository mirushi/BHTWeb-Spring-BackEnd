package com.bhtcnpm.website.security.util;

import com.bhtcnpm.website.constant.security.SecurityConstant;
import com.bhtcnpm.website.model.entity.UserWebsiteEntities.RefreshToken;
import org.springframework.http.HttpHeaders;

public class SecurityUtils {

    public static HttpHeaders getJwtRefreshTokenHeader(HttpHeaders headers, RefreshToken refreshToken) {
        if (headers == null) {
            headers = new HttpHeaders();
        }
        headers.add(SecurityConstant.JWT_REFRESH_TOKEN_HEADER, refreshToken.getToken());

        return headers;
    }
}
