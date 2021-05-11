package com.bhtcnpm.website.config;

import com.bhtcnpm.website.constant.security.SecurityConstant;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
@Profile("dev")
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        //The prePostEnabled property enables Spring Security pre/post annotations
        prePostEnabled = true,
        //The securedEnabled property determines if the @Secured annotation should be enabled
        securedEnabled = true,
        //The jsr250Enabled property allows us to use the @RoleAllowed annotation
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    //Variable for specifying whenever Authentication and Authorization should be applied or not.
    private Boolean isSecurityEnabled = false;

    @Qualifier("jpaUserDetailsService")
    private final UserDetailsService jpaUserDetailsService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors() //Enable CSRF for JWT.
            .and()
            //JWT is stateless. We don't do tracking.
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            //Allow all public URLs.
            .authorizeRequests().antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
            .and()
            //TODO: Remove this before applying to production environment. Below is for development profile only.
            .authorizeRequests().antMatchers(SecurityConstant.DEV_PUBLIC_URLS).permitAll();

        if (isSecurityEnabled == true) {
            //All other requests must be authenticated.
            http.authorizeRequests().anyRequest().authenticated();
        } else {
            http.authorizeRequests().anyRequest().permitAll();
        }

        //Allow frame for H2 console.
        //TODO: Remove this before applying to production environment. Below is for development profile only.
        http.headers().frameOptions().sameOrigin();
    }
}
