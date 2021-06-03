package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryMapper;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper (
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = Collectors.class,
        uses = {UserSummaryMapper.class}
)
public abstract class UserPostReportMapper {

    protected UserSummaryMapper userSummaryMapper;

    public List<UserSummaryDTO> getUserSummaryDTOListFromUserPostReportList (List<UserPostReport> userPostReports) {
        return userPostReports.stream().map(
                report -> userSummaryMapper.userWebsiteToUserSummaryDTO(report.getUserPostReportId().getUser())
        ).collect(Collectors.toList());
    }

    public Set<ReportReasonDTO> getReportReasonDTOSetFromUserPostReportList (List<UserPostReport> userPostReports) {
        //TODO: Please implement this to return aggregate of all report reasons.
        return null;
    }

    @Autowired
    public void setUserSummaryMapper (UserSummaryMapper userSummaryMapper) {
        this.userSummaryMapper = userSummaryMapper;
    }
}
