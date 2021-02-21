package com.bhtcnpm.website.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bhtcnpm.website.constant.security.SecurityConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(UserWebsite userWebsite) {
        String[] claims = getClaimsFromUser(userWebsite);

        return JWT.create()
                .withIssuer(SecurityConstant.BHTCNPM_ORG)
                .withAudience(SecurityConstant.BHTCNPM_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(userWebsite.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .withArrayClaim(SecurityConstant.AUTHORITIES, claims)
                .sign(Algorithm.HMAC512(secret));
    }

    public List<GrantedAuthority> getAuthorities (String token) {
        //Will throw exception if token is invalid.
        String[] claims = getClaimsFromToken(token);

        if (claims == null) {
            return null;
        }

        return Arrays.stream(claims)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Authentication getAuthentication (String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken userPassAuthToken
                = new UsernamePasswordAuthenticationToken(username, null, authorities);
        userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPassAuthToken;
    }

    public boolean isTokenValid (String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }

    public String getSubject (String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    private boolean isTokenExpired (JWTVerifier verifier, String token) {
        Date expirationDate = verifier.verify(token).getExpiresAt();
        return expirationDate.before(new Date());
    }

    private String[] getClaimsFromToken (String token) {
        JWTVerifier jwtVerifier = getJWTVerifier();
        return jwtVerifier
                .verify(token)
                .getClaim(SecurityConstant.AUTHORITIES)
                .asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier jwtVerifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            jwtVerifier = JWT.require(algorithm).withIssuer(SecurityConstant.BHTCNPM_ORG).build();
        } catch (JWTVerificationException jwtVerificationException) {
            throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
        return jwtVerifier;
    }

    private String[] getClaimsFromUser(UserWebsite userPrincipal) {
        Set<GrantedAuthority> grantedAuthorities = userPrincipal.getAuthorities();

        String[] authorities = new String[grantedAuthorities.size()];

        int indexCounter = 0;
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            authorities[indexCounter++] = grantedAuthority.getAuthority();
        }

        return authorities;
    }
}
