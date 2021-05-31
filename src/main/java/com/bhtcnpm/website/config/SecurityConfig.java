package com.bhtcnpm.website.config;

import com.bhtcnpm.website.constant.security.SecurityConstant;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.filter.LoadUserDetailsFilter;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.security.Security;

@KeycloakConfiguration
@Profile("dev")
//TODO: Please disable debug before goes production.
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    //Variable for specifying whenever Authentication and Authorization should be applied or not.
    private Boolean isSecurityEnabled = false;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private UserWebsiteRepository userWebsiteRepository;

    /**
     * Registers the KeycloakAuthenticationProvider with the authentication manager.
     */
    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    /**
     * Use application.properties for Keycloak configuration.
     */

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    /**
     * Custom filter for account retrieval and/or creation.
     * **/
    @Bean
    protected LoadUserDetailsFilter loadUserDetailsFilter() {
        return new LoadUserDetailsFilter(userWebsiteRepository);
    }

    /**
     * Prevent double bean registration.
     * */
    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakAuthenticatedActionsFilterBean(
            KeycloakAuthenticatedActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakSecurityContextRequestFilterBean(
            KeycloakSecurityContextRequestFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Override
    //TODO: Remove this before applying to production environment. Below is for development profile only.
    public void configure (WebSecurity web) throws Exception {
        web.ignoring().antMatchers(SecurityConstant.DEV_PUBLIC_URLS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Use Keycloak configuration too.
        super.configure(http);

        //Disable CSRF because this application is REST API only (aka stateless).
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //Allow all public URLs.
                .authorizeRequests().antMatchers(SecurityConstant.PUBLIC_URLS).permitAll();
//        if (isSecurityEnabled == true) {
//            //All other requests must be authenticated.
//            http.authorizeRequests().anyRequest().authenticated();
//        } else {
//            http.authorizeRequests().anyRequest().permitAll();
//        }

        //Filter to push user data into SecurityContext.
        //The SecurityConfig of Keycloak will be applied after this configuration.
        http.addFilterAfter(loadUserDetailsFilter(), KeycloakAuthenticatedActionsFilter.class);
    }
}
