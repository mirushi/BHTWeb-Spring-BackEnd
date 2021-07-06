package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.entity.DocCommentEntities.report.UserDocCommentReport;
import com.bhtcnpm.website.model.entity.DocEntities.report.UserDocReport;
import com.bhtcnpm.website.model.entity.ExerciseEntities.UserExerciseReport;
import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentReport;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
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

    default List<UserSummaryDTO> userExerciseReportListToUserSummaryDTOList (List<UserExerciseReport> userExerciseReportList) {
        List<UserSummaryDTO> userSummaryDTOs = userWebsiteListToUserSummaryDTOList(
                userExerciseReportList.stream()
                .map(obj -> obj.getUserExerciseReportId().getUser())
                .collect(Collectors.toList())
        );

        return userSummaryDTOs;
    }

    default List<UserSummaryDTO> userDocReportListToUserSummaryDTOList (List<UserDocReport> userDocReportList) {
        return userWebsiteListToUserSummaryDTOList(
                userDocReportList.stream()
                .map(obj -> obj.getUserDocReportId().getUser())
                .collect(Collectors.toList())
        );
    }

    default List<UserSummaryDTO> userDocCommentReportListToUserSummaryDTOList (List<UserDocCommentReport> userDocCommentReportList) {
        return userWebsiteListToUserSummaryDTOList(
                userDocCommentReportList.stream()
                .map(obj -> obj.getUserDocCommentReportId().getUser())
                .collect(Collectors.toList())
        );
    }

    @Mapping(target = "id", source = "userWebsite.id")
    @Mapping(target = "name", source = "userWebsite.name")
    @Mapping(target = "displayName", source = "userWebsite.displayName")
    @Mapping(target = "reputationScore", source = "userWebsite.reputationScore")
    @Mapping(target = "avatarURL", source = "userWebsite.avatarURL")
    @Mapping(target = "postCount", source = "statisticDTO.postCount")
    @Mapping(target = "docCount", source = "statisticDTO.docCount")
    UserSummaryWithStatisticDTO userWebsiteToUserWebsiteSummaryWithStatisticDTO(UserWebsite userWebsite, UserStatisticDTO statisticDTO);

    @Mapping(target = "id", source = "userWebsite.id")
    @Mapping(target = "name", source = "userWebsite.name")
    @Mapping(target = "displayName", source = "userWebsite.displayName")
    @Mapping(target = "reputationScore", source = "userWebsite.reputationScore")
    @Mapping(target = "avatarURL", source = "userWebsite.avatarURL")
    @Mapping(target = "email", source = "userWebsite.email")
    @Mapping(target = "aboutMe", source = "userWebsite.aboutMe")
    @Mapping(target = "postCount", source = "statisticDTO.postCount")
    @Mapping(target = "docCount", source = "statisticDTO.docCount")
    UserDetailsWithStatisticDTO userWebsiteToUserWebsiteDetailsWithStatisticDTO(UserWebsite userWebsite, UserStatisticDTO statisticDTO);

    @Mapping(target = "id", source = "userWebsite.id")
    @Mapping(target = "name", source = "userWebsite.name")
    @Mapping(target = "displayName", source = "userWebsite.displayName")
    @Mapping(target = "reputationScore", source = "userWebsite.reputationScore")
    @Mapping(target = "avatarURL", source = "userWebsite.avatarURL")
    @Mapping(target = "email", source = "userWebsite.email")
    @Mapping(target = "aboutMe", source = "userWebsite.aboutMe")
    UserDetailsDTO userWebsiteToUserDetailsDTO (UserWebsite userWebsite);

    @Mapping(target = "displayName", source = "displayName")
    @Mapping(target = "aboutMe", source = "aboutMe")
    void updateUserWebsiteFromUserRequestDTO (UserRequestDTO userRequestDTO, @MappingTarget UserWebsite userWebsite);

}
