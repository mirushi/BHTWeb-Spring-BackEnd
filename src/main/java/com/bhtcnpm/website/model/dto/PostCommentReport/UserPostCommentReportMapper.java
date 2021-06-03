package com.bhtcnpm.website.model.dto.PostCommentReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryMapper;
import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentReport;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = Collectors.class,
        uses = {UserSummaryMapper.class}
)
public abstract class UserPostCommentReportMapper {
    protected UserSummaryMapper userSummaryMapper;

    public List<UserSummaryDTO> getUserSummaryDTOListFromUserPostCommentReportList (List<UserPostCommentReport> userPostCommentReports) {
        return userPostCommentReports.stream().map(
                report -> userSummaryMapper.userWebsiteToUserSummaryDTO(report.getUserPostCommentReportId().getUser())
        ).collect(Collectors.toList());
    }

    public Set<ReportReasonDTO> getReportReasonDTOSetFromUserPostCommentReportList (List<UserPostCommentReport> userPostCommentReports) {
        //TODO: Please implement this to return aggregate of all report reasons.
        return null;
    }

    @Autowired
    public void setUserSummaryMapper (UserSummaryMapper userSummaryMapper) {
        this.userSummaryMapper = userSummaryMapper;
    }

}
