package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryMapper;
import com.bhtcnpm.website.model.entity.PostEntities.PostReport;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {UserSummaryMapper.class})
public abstract class UserPostReportMapper {
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "reporter", source = "postReport.reporter")
    @Mapping(target = "title", source = "postReport.post.title")
    @Mapping(target = "content", source = "postReport.post.content")
    @Mapping(target = "postImageURL", source = "postReport.post.imageURL")
    public abstract UserPostReportDTO userPostReportToUserPostReportDTO (PostReport postReport);

    public abstract List<UserPostReportDTO> userPostReportListToUserPostReportDTOList (List<PostReport> postReports);

    public UserPostReportListDTO userPostReportPageToUserPostReportListDTO (Page<PostReport> userPostReports) {
        UserPostReportListDTO dto = new UserPostReportListDTO();
        dto.setUserPostReportDTOs(this.userPostReportListToUserPostReportDTOList(userPostReports.getContent()));
        dto.setTotalPages(userPostReports.getTotalPages());
        dto.setTotalElements(userPostReports.getTotalElements());

        return dto;
    }
}
