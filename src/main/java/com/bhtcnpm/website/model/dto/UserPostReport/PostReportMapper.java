package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonMapper;
import com.bhtcnpm.website.model.dto.UserWebsite.UserMapper;
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
        uses = {UserMapper.class,
                ReportReasonMapper.class})
public interface PostReportMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "postID", source = "post.id")
    @Mapping(target = "postImageURL", source = "postReport.post.imageURL")
    @Mapping(target = "title", source = "postReport.post.title")
    @Mapping(target = "content", source = "postReport.post.content")
    @Mapping(target = "author", source = "postReport.post.author")
    @Mapping(target = "reporters",
            source = "postReport.userPostReports")
    @Mapping(target = "reportReasons", source = "postReport.userPostReports")
    @Mapping(target = "feedbacks", expression = "java(" +
            "postReport.getUserPostReports()" +
            ".stream()" +
            ".map(obj -> obj.getFeedback())" +
            ".collect(Collectors.toList())" +
            ")")
    @Mapping(target = "reportTime", source = "reportTime")
    @Mapping(target = "resolvedTime", source = "resolvedTime")
    @Mapping(target = "resolvedNote", source = "resolvedNote")
    @Mapping(target = "resolvedBy", source = "resolvedBy")
    @Mapping(target = "actionTaken", source = "actionTaken")
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
