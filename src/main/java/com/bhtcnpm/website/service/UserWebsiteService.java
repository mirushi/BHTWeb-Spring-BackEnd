package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserWebsiteService {
    @PreAuthorize(value = "isAuthenticated()")
    UserSummaryWithStatisticDTO getUserSummaryWithStatistic(Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    UserFullStatisticDTO getUserStatisticDTO (UUID userID);

    @PreAuthorize(value = "isAuthenticated()")
    UserDetailsWithStatisticDTO getUserDetailsOwnWithStatistic(Authentication authentication);

    @PreAuthorize(value = "hasPermission(#userID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).USER_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.UserWebsiteActionPermissionRequest).READ_DETAIL_PERMISSION)")
    UserDetailsWithStatisticDTO getSpecificUserDetailsWithStatistic (UUID userID);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.UserWebsitePermissionConstant).USER_ALL_SELF_UPDATE)")
    UserDetailsDTO putUserDetails (@Valid UserRequestDTO userRequestDTO, Authentication authentication);

    @PreAuthorize(value = "isAuthenticated()")
    UserDetailsDTO putUserAvatarImage (MultipartFile multipartFile, Authentication authentication) throws FileExtensionNotAllowedException, IOException;

    @PreAuthorize(value = "hasPermission(#userID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).USER_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.UserWebsiteActionPermissionRequest).UPDATE_PERMISSION)")
    UserDetailsDTO putSpecificUserDetails (@Valid UserRequestDTO userRequestDTO, UUID userID);

    @PreAuthorize(value = "permitAll()")
    List<UserWebsiteAvailableActionDTO> getUserWebsiteAvailableAction (List<UUID> userIDs, Authentication authentication);

    @PreAuthorize(value = "isAuthenticated()")
    boolean addUserReputationScore(UUID authorID, ReputationType reputationType, long count);

    @PreAuthorize(value = "isAuthenticated()")
    boolean subtractUserReputationScore (UUID authorID, ReputationType reputationType, long count);
}
