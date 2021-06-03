package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentReport;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserSummaryMapper {
    UserSummaryDTO userWebsiteToUserSummaryDTO (UserWebsite userWebsite);

    List<UserSummaryDTO> userWebsiteListToUserSummaryDTOList (List<UserWebsite> userWebsiteList);

    default List<UserSummaryDTO> userPostReportListToUserSummaryDTOList (List<UserPostReport> userPostReportList) {
        List<UserSummaryDTO> userSummaryDTOs = userWebsiteListToUserSummaryDTOList(
                userPostReportList.stream()
                .map(obj -> obj.getUserPostReportId().getUser())
                .collect(Collectors.toList())
        );

        return userSummaryDTOs;
    }

    default List<UserSummaryDTO> userPostCommentReportListTouserSummaryDTOList (List<UserPostCommentReport> userPostCommentReportList) {
        List<UserSummaryDTO> userSummaryDTOs = userWebsiteListToUserSummaryDTOList(
                userPostCommentReportList.stream()
                .map(obj -> obj.getUserPostCommentReportId().getUser())
                .collect(Collectors.toList())
        );

        return userSummaryDTOs;
    }
}
