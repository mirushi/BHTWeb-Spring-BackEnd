package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.api.amazon.s3.AmazonS3Constant;
import com.bhtcnpm.website.constant.business.UserWebsite.UserWebsiteActionAvailableConstant;
import com.bhtcnpm.website.constant.security.evaluator.permission.UserWebsiteActionPermissionRequest;
import com.bhtcnpm.website.model.dto.AWS.AmazonS3ResultDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.evaluator.UserWebsite.UserWebsitePermissionEvaluator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.FileUploadService;
import com.bhtcnpm.website.service.UserWebsiteService;
import com.bhtcnpm.website.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final UserWebsitePermissionEvaluator userWebsitePermissionEvaluator;
    private final FileUploadService fileUploadService;

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
    public UserFullStatisticDTO getUserStatisticDTO(UUID userID) {
        UserFullStatisticDTO statisticDTO = uwRepository.getUserWebsiteFullStatistic(userID);
        return statisticDTO;
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
    public UserDetailsDTO putUserAvatarImage(MultipartFile multipartFile, Authentication authentication) throws FileExtensionNotAllowedException, IOException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        String key = FileUploadUtils.getS3AvatarURLUploadKey(userID, multipartFile);

        AmazonS3ResultDTO result = fileUploadService.uploadImageToS3(key, multipartFile);

        String newAvatarURL = result.getDirectURL();
        UserWebsite user = uwRepository.getOne(userID);
        user.setAvatarURL(newAvatarURL);
        user = uwRepository.save(user);

        return userMapper.userWebsiteToUserDetailsDTO(user);
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

    @Override
    public List<UserWebsiteAvailableActionDTO> getUserWebsiteAvailableAction(List<UUID> userIDs, Authentication authentication) {
        List<UserWebsiteAvailableActionDTO> userWebsiteAvailableActionDTOList = new ArrayList<>();

        for (UUID userID : userIDs) {
            if (userID == null) {
                continue;
            }

            UserWebsiteAvailableActionDTO userWebsiteAvailableActionDTO = new UserWebsiteAvailableActionDTO();
            userWebsiteAvailableActionDTO.setId(userID);
            List<String> availableActionList = new ArrayList<>();

            if (userWebsitePermissionEvaluator.hasPermission(authentication, userID, UserWebsiteActionPermissionRequest.READ_DETAIL_PERMISSION)) {
                availableActionList.add(UserWebsiteActionAvailableConstant.READ_ACTION);
            }

            if (userWebsitePermissionEvaluator.hasPermission(authentication, userID, UserWebsiteActionPermissionRequest.UPDATE_PERMISSION)) {
                availableActionList.add(UserWebsiteActionAvailableConstant.UPDATE_ACTION);
            }

            userWebsiteAvailableActionDTO.setAvailableActions(availableActionList);
            userWebsiteAvailableActionDTOList.add(userWebsiteAvailableActionDTO);
        }

        return userWebsiteAvailableActionDTOList;
    }

}
