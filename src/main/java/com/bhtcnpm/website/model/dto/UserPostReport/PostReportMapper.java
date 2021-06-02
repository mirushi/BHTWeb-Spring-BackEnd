package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonMapper;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryMapper;
import com.bhtcnpm.website.model.entity.PostEntities.PostReport;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = Collectors.class,
        uses = {UserSummaryMapper.class,
                ReportReasonMapper.class,
                UserPostReportMapper.class})
public interface PostReportMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "reporters",
            source = "postReport.userPostReports")
    @Mapping(target = "title", source = "postReport.post.title")
    @Mapping(target = "content", source = "postReport.post.content")
    @Mapping(target = "postImageURL", source = "postReport.post.imageURL")
    @Mapping(target = "feedbacks", expression = "java(" +
            "postReport.getUserPostReports()" +
            ".stream()" +
            ".map(obj -> obj.getFeedback())" +
            ".collect(Collectors.toList())" +
            ")")
    @Mapping(target = "reportReasons", source = "postReport.userPostReports")
    PostReportDTO userPostReportToUserPostReportDTO (PostReport postReport);

    List<PostReportDTO> userPostReportListToUserPostReportDTOList (List<PostReport> postReports);

    default UserPostReportListDTO userPostReportPageToUserPostReportListDTO (Page<PostReport> userPostReports) {
        UserPostReportListDTO dto = new UserPostReportListDTO();
        dto.setPostReportDTOS(this.userPostReportListToUserPostReportDTOList(userPostReports.getContent()));
        dto.setTotalPages(userPostReports.getTotalPages());
        dto.setTotalElements(userPostReports.getTotalElements());

        return dto;
    }
}
