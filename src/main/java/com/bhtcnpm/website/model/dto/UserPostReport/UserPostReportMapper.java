package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryMapper;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {UserSummaryMapper.class})
public abstract class UserPostReportMapper {
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "reporter", source = "userPostReport.reporter")
    public abstract UserPostReportDTO userPostReportToUserPostReportDTO (UserPostReport userPostReport);

    public abstract List<UserPostReportDTO> userPostReportListToUserPostReportDTOList (List<UserPostReport> userPostReports);

    public UserPostReportListDTO userPostReportPageToUserPostReportListDTO (Page<UserPostReport> userPostReports) {
        UserPostReportListDTO dto = new UserPostReportListDTO();
        dto.setUserPostReportDTOs(this.userPostReportListToUserPostReportDTOList(userPostReports.getContent()));
        dto.setTotalPages(userPostReports.getTotalPages());
        dto.setTotalElements(userPostReports.getTotalElements());

        return dto;
    }
}
