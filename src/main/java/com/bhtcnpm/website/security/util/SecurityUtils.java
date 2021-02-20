package com.bhtcnpm.website.security.util;

import com.bhtcnpm.website.config.SecurityConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityUtils {
    public static String getDefaultEncodingPrefixedPassword (String password) {
        return StringUtils.prependIfMissingIgnoreCase(password, SecurityConfig.DEFAULT_ENCODING_ALGO);
    }

    public static String getEncodedPassword (String password, PasswordEncoder passwordEncoder) {
        String prefixedPassword = getDefaultEncodingPrefixedPassword(password);
        return passwordEncoder.encode(prefixedPassword);
    }
}
