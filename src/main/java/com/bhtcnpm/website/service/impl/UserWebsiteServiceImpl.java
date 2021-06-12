package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
@Slf4j
public class UserWebsiteServiceImpl implements UserWebsiteService {

    //Repositories
    private final UserWebsiteRepository uwRepository;
    private final UserMapper userMapper;

    @Override
    public UserSummaryWithStatisticDTO getUserSummaryWithStatistic(Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        if (!(authentication.getDetails() instanceof SimpleKeycloakAccountWithEntity)) {
            throw new IllegalArgumentException("Unsupported authentication token.");
        }

        SimpleKeycloakAccountWithEntity kcAccount = (SimpleKeycloakAccountWithEntity) authentication.getDetails();

        UserWebsite userWebsiteEntity = kcAccount.getEntity();

        UserStatisticDTO statisticDTO = uwRepository.getUserWebsiteStatistic(userID);

        UserSummaryWithStatisticDTO userSummaryWithStatisticDTO = userMapper.userWebsiteToUserWebsiteSummaryWithStatisticDTO(userWebsiteEntity, statisticDTO);

        return userSummaryWithStatisticDTO;
    }

    @Override
    public UserDetailsWithStatisticDTO getUserDetailsOwnWithStatistic(Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        if (!(authentication.getDetails() instanceof SimpleKeycloakAccountWithEntity)) {
            throw new IllegalArgumentException("Unsupported authentication token.");
        }

        SimpleKeycloakAccountWithEntity kcAccount = (SimpleKeycloakAccountWithEntity) authentication.getDetails();

        UserWebsite userWebsiteEntity = kcAccount.getEntity();

        UserStatisticDTO statisticDTO = uwRepository.getUserWebsiteStatistic(userID);

        UserDetailsWithStatisticDTO userDetailsWithStatisticDTO = userMapper.userWebsiteToUserWebsiteDetailsWithStatisticDTO(userWebsiteEntity, statisticDTO);

        return userDetailsWithStatisticDTO;
    }

    @Override
    public UserDetailsWithStatisticDTO getSpecificUserDetailsWithStatistic(UUID userID) {
        Optional<UserWebsite> userWebsite = uwRepository.findById(userID);

        if (userWebsite.isEmpty()) {
            throw new IDNotFoundException();
        }

        UserWebsite entity = userWebsite.get();

        UserStatisticDTO statisticDTO = uwRepository.getUserWebsiteStatistic(entity.getId());

        UserDetailsWithStatisticDTO userDetailsWithStatisticDTO = userMapper.userWebsiteToUserWebsiteDetailsWithStatisticDTO(entity, statisticDTO);

        return userDetailsWithStatisticDTO;
    }

    @Override
    public UserDetailsDTO putUserDetails(UserRequestDTO userRequestDTO, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);
        Optional<UserWebsite> object = uwRepository.findById(userID);

        if (object.isEmpty()) {
            throw new IllegalArgumentException("userID not found.");
        }

        UserWebsite entity = object.get();

        userMapper.updateUserWebsiteFromUserRequestDTO(userRequestDTO, entity);

        entity = uwRepository.save(entity);

        return userMapper.userWebsiteToUserDetailsDTO(entity);
    }

    @Override
    public UserDetailsDTO putSpecificUserDetails(UserRequestDTO userRequestDTO, UUID userID) {
        Optional<UserWebsite> object = uwRepository.findById(userID);

        if (object.isEmpty()) {
            throw new IDNotFoundException();
        }

        UserWebsite entity = object.get();
        userMapper.updateUserWebsiteFromUserRequestDTO(userRequestDTO, entity);
        entity = uwRepository.save(entity);

        return userMapper.userWebsiteToUserDetailsDTO(entity);
    }

}
