package com.bhtcnpm.website.security.util;

import com.bhtcnpm.website.config.SecurityConfig;
import com.bhtcnpm.website.constant.security.SecurityConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.security.JwtTokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityUtils {
    public static String getDefaultEncodingPrefixedPassword (String password) {
        return StringUtils.prependIfMissingIgnoreCase(password, SecurityConfig.DEFAULT_ENCODING_ALGO);
    }

    public static String getEncodedPassword (String password, PasswordEncoder passwordEncoder) {
        String prefixedPassword = getDefaultEncodingPrefixedPassword(password);
        return passwordEncoder.encode(prefixedPassword);
    }

    public static HttpHeaders getJwtHeader (UserWebsite userWebsite, JwtTokenProvider jwtTokenProvider) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userWebsite));

        return headers;
    }

    public static void authenticateUser (String username, String normalPassword, AuthenticationManager authManager) {
        String prefixedPassword = SecurityUtils.getDefaultEncodingPrefixedPassword(normalPassword);
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, prefixedPassword));
    }
}
