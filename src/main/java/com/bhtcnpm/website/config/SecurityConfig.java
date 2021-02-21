package com.bhtcnpm.website.config;

import com.bhtcnpm.website.constant.security.SecurityConstant;
import com.bhtcnpm.website.security.filter.JwtAccessDeniedHandler;
import com.bhtcnpm.website.security.filter.JwtAuthenticationEntryPoint;
import com.bhtcnpm.website.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("dev")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //Variable for specifying whenever Authentication and Authorization should be applied or not.
    private Boolean isSecurityEnabled = false;

    public static final String DEFAULT_ENCODING_ALGO = "{bcrypt}";

    @Qualifier("jpaUserDetailsService")
    private final UserDetailsService jpaUserDetailsService;

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public PasswordEncoder passwordEncoder () {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //For use with Spring Data JPA SPeL.
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
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

        http.exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        //Allow frame for H2 console.
        //TODO: Remove this before applying to production environment. Below is for development profile only.
        http.headers().frameOptions().sameOrigin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jpaUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean () throws Exception {
        return super.authenticationManagerBean();
    }

}
