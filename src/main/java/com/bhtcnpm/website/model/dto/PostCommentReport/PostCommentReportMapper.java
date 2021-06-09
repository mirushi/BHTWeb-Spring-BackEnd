package com.bhtcnpm.website.model.dto.PostCommentReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonMapper;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryMapper;
import com.bhtcnpm.website.model.entity.PostCommentEntities.PostCommentReport;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = Collectors.class,
        uses = {UserSummaryMapper.class, ReportReasonMapper.class}
)
public interface PostCommentReportMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "commentID", source = "postComment.id")
    @Mapping(target = "author", source = "postComment.author")
    @Mapping(target = "postID", source = "postComment.post.id")
    @Mapping(target = "postTitle", source = "postComment.post.title")
    @Mapping(target = "content", source = "postComment.content")
    @Mapping(target = "reporters", source = "userPostCommentReports")
    @Mapping(target = "reportReasons", source = "userPostCommentReports")
    @Mapping(target = "feedbacks", expression = "java(" +
            "postCommentReport.getUserPostCommentReports()" +
            ".stream()" +
            ".map(obj -> obj.getFeedback())" +
            ".collect(Collectors.toList())" +
            ")")
    @Mapping(target = "reportTime", source = "reportTime")
    @Mapping(target = "resolvedTime", source = "resolvedTime")
    @Mapping(target = "resolvedNote", source = "resolvedNote")
    @Mapping(target = "resolvedBy", source = "resolvedBy")
    @Mapping(target = "actionTaken", source = "actionTaken")
    PostCommentReportDTO userPostCommentReportToUserPostCommentReportDTO (PostCommentReport postCommentReport);

    List<PostCommentReportDTO> userPostCommentReportListToUserPostCommentReportDTOList (List<PostCommentReport> postCommentReports);

    default PostCommentReportListDTO userPostCommentReportPageToUserPostCommentReportListDTO (Page<PostCommentReport> userPostCommentReports) {
        PostCommentReportListDTO dto = new PostCommentReportListDTO();
        dto.setPostCommentReportDTOs(this.userPostCommentReportListToUserPostCommentReportDTOList(userPostCommentReports.getContent()));
        dto.setTotalPages(userPostCommentReports.getTotalPages());
        dto.setTotalElements(userPostCommentReports.getTotalElements());

        return dto;
    }
}
