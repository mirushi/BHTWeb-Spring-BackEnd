package com.bhtcnpm.website.security.service;

//@Slf4j
//@RequiredArgsConstructor
//@Transactional
//@Qualifier("jpaUserDetailsService")
//@Service
//public class JpaUserDetailsService implements UserDetailsService {
//
//    private final UserWebsiteRepository userWebsiteRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userWebsiteRepository.findByName(username).orElseThrow(() -> {
//            log.error("User not found by username: " + username);
//            return new UsernameNotFoundException("User name: " + username + " not found");
//        });
//    }
//}
