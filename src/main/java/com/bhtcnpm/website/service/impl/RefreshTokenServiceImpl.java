package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.business.UserWebsite.VTBusinessConstant;
import com.bhtcnpm.website.constant.domain.UserWebsite.RTDomainConstant;
import com.bhtcnpm.website.constant.security.SecurityConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.UserWebsiteEntities.RefreshToken;
import com.bhtcnpm.website.repository.RefreshTokenRepository;
import com.bhtcnpm.website.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createNewRefreshToken(UserWebsite userWebsite) {
        //First we have to check if current number of refresh token for user is already exceeded maximum.
        int refreshTokenCount = refreshTokenRepository.refreshTokenCountOfUser(userWebsite);
        if (refreshTokenCount >= SecurityConstant.MAXIMUM_REFRESH_TOKEN_PER_USER) {
            throw new IllegalArgumentException("You have too many session open. Please log out some and try again.");
        }

        //Generate secure token.
        SecureRandom random = new SecureRandom();

        //Generate token.
        String token = new BigInteger(
                RTDomainConstant.REFRESH_TOKEN_LENGTH * 5,
                random
        ).toString(32);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(userWebsite)
                .build();

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Override
    public RefreshToken getRefreshToken(String refreshToken) {
        Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findById(refreshToken);

        if (!refreshTokenEntity.isPresent()) {
            throw new IllegalArgumentException("Your refresh token is invalid.");
        }

        return refreshTokenEntity.get();
    }
}
