package com.bhtcnpm.website.security.util;

import com.bhtcnpm.website.model.dto.UserWebsite.SimpleKeycloakAccountWithEntity;
import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class SecurityUtils {

    public static UserWebsite getUserWebsiteEntityFromAuthenticationToken (Authentication authentication) {
        if (!(authentication.getDetails() instanceof SimpleKeycloakAccountWithEntity)) {
            throw new IllegalArgumentException("Unsupported authentication token.");
        }

        SimpleKeycloakAccountWithEntity kcAccount = (SimpleKeycloakAccountWithEntity) authentication.getDetails();

        return kcAccount.getEntity();
    }

    public static UUID getUserIDOnNullThrowException(Authentication authentication) {
        UUID userID = getUserID(authentication);
        if (userID == null) {
            throw new AccessDeniedException("Cannot extract user ID from authentication.");
        }
        return userID;
    }

    public static UUID getUserID (Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        if (!(authentication instanceof KeycloakAuthenticationToken)) {
            return null;
        }
        if (!(authentication.getPrincipal() instanceof Principal)) {
            return null;
        }
        Principal principalObj = (Principal) authentication.getPrincipal();
        if (principalObj == null) {
            return null;
        }
        return UUID.fromString(principalObj.getName());
    }

    public static boolean containsAuthority (Authentication authentication, String authority) {
        if (authority == null || authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(authority));
    }

    public static boolean containsAuthorities (Authentication authentication, String... authorities) {
        if (authorities == null || authorities.length == 0) {
            return false;
        }
        if (authentication == null) {
            return false;
        }
        if (authentication.getAuthorities() == null) {
            return false;
        }

        return authentication.getAuthorities().containsAll(
                Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
        );
    }

    public static boolean isAnonymousUser (Authentication authentication) {
        if (authentication == null || authentication != null && "anonymousUser".equals(authentication.toString())) {
            return true;
        }
        return false;
    }
}
