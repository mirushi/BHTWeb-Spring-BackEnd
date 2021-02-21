package com.bhtcnpm.website.security.service;

import com.bhtcnpm.website.repository.UserWebsiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Qualifier("jpaUserDetailsService")
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserWebsiteRepository userWebsiteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userWebsiteRepository.findByName(username).orElseThrow(() -> {
            log.error("User not found by username: " + username);
            return new UsernameNotFoundException("User name: " + username + " not found");
        });
    }
}
