package com.bhtcnpm.website.security.filter;

import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstant;
import com.bhtcnpm.website.model.dto.UserWebsite.SimpleKeycloakAccountWithEntity;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class LoadUserDetailsFilter extends GenericFilterBean {

    private static final String FILTER_APPLIED = LoadUserDetailsFilter.class.getName();

    private final UserWebsiteRepository userWebsiteRepository;

    public LoadUserDetailsFilter(UserWebsiteRepository userWebsiteRepository) {
        this.userWebsiteRepository = userWebsiteRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        //The load user filter should be applied only once.
//        if(servletRequest.getAttribute(FILTER_APPLIED) != null) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        servletRequest.setAttribute(FILTER_APPLIED, Boolean.TRUE);
        log.debug("LoadUserDetailsFilter called!");

        SecurityContext securityContext = SecurityContextHolder.getContext();

        //The context is null, meaning that user don't provide any credentials (aka anonymous).
        if (securityContext == null || securityContext.getAuthentication() == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //The principal is not keycloak, this filter only makes sense with keycloak.
        if (!(securityContext.getAuthentication() instanceof KeycloakAuthenticationToken)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //If the account already loaded with entity, we don't need to proceed any further.
        Object authenticationDetails = securityContext.getAuthentication().getDetails();
        if (authenticationDetails instanceof SimpleKeycloakAccountWithEntity
                && ((SimpleKeycloakAccountWithEntity) authenticationDetails).getEntity() != null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        KeycloakPrincipal principal = (KeycloakPrincipal)securityContext.getAuthentication().getPrincipal();
        KeycloakSecurityContext ctx = principal.getKeycloakSecurityContext();

        String userID = ctx.getToken().getSubject();

        Optional<UserWebsite> currentUser = userWebsiteRepository.findById(UUID.fromString(userID));

        if (currentUser.isEmpty()) {
            UserWebsite newUser = UserWebsite.builder()
                    .id(UUID.fromString(userID))
                    .name(ctx.getToken().getPreferredUsername())
                    .email(ctx.getToken().getEmail())
                    .displayName(ctx.getToken().getName())
                    .reputationScore(UWBusinessConstant.DEFAULT_REPUTATION_SCORE)
                    .avatarURL(UWBusinessConstant.DEFAULT_AVATAR_URL)
                    .version((short)0)
                    .build();
            userWebsiteRepository.save(newUser);
            currentUser = Optional.of(newUser);
        }

        //Add entity to context.
        //Make sure that the class is SimpleKeycloakAccount.
        Assert.isTrue(securityContext.getAuthentication().getDetails() instanceof SimpleKeycloakAccount,
                "User details have to be SimpleKeycloakAccount instance.");
        ((KeycloakAuthenticationToken)securityContext.getAuthentication())
                .setDetails(
                        new SimpleKeycloakAccountWithEntity(
                                (SimpleKeycloakAccount) securityContext.getAuthentication().getDetails(),
                                currentUser.get()
                        )
                );

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
