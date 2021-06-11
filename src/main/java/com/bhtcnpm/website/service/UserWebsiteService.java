package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserWebsite.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface UserWebsiteService {
    @PreAuthorize(value = "isAuthenticated()")
    UserSummaryWithStatisticDTO getUserSummaryWithStatistic(Authentication authentication);

    @PreAuthorize(value = "isAuthenticated()")
    UserDetailsWithStatisticDTO getUserDetailsOwnWithStatistic(Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.UserWebsitePermissionConstant).USER_ALL_ALL_READ)")
    UserDetailsWithStatisticDTO getSpecificUserDetailsWithStatistic (UUID userID);
}
