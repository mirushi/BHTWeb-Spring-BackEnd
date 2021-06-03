package com.bhtcnpm.website.model.dto.ReportReason;

import com.bhtcnpm.website.model.entity.PostCommentEntities.PostCommentReportReason;
import com.bhtcnpm.website.model.entity.PostCommentEntities.UserPostCommentReport;
import com.bhtcnpm.website.model.entity.PostEntities.PostReportReason;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper
public interface ReportReasonMapper {
    ReportReasonMapper INSTANCE = Mappers.getMapper(ReportReasonMapper.class);

    ReportReasonDTO reportReasonToReportReasonDTO (ReportReason reportReason);

    List<ReportReasonDTO> reportReasonListToReportReasonDTOList (List <ReportReason> reportReasonList);

    @Mapping(target = "id", source = "postReportReason.postReportReasonId.reportReason.id")
    @Mapping(target = "reason", source = "postReportReason.postReportReasonId.reportReason.reason")
    ReportReasonDTO postReportReasonToReportReasonDTO (PostReportReason postReportReason);

    List<ReportReasonDTO> postReportReasonToReportReasonDTOs (List<PostReportReason> postReportReason);

    Set<ReportReasonDTO> postReportReasonListToReportReasonDTOsSet (List<PostReportReason> postReportReasons);

    @Mapping(target = "id", source = "postCommentReportReason.postCommentReportReasonId.reportReason.id")
    @Mapping(target = "reason", source = "postCommentReportReason.postCommentReportReasonId.reportReason.reason")
    ReportReasonDTO postCommentReportReasonToReportReasonDTO (PostCommentReportReason postCommentReportReason);

    Set<ReportReasonDTO> postCommentReportReasonListToReportReasonDTOsSet (List<PostCommentReportReason> postCommentReportReasons);

    default Set<ReportReasonDTO> userPostReportToReportReasonDTO (List<UserPostReport> userPostReportList) {
        Set<ReportReasonDTO> finalResult = new HashSet<>();

        for (UserPostReport upr : userPostReportList) {
            Set<ReportReasonDTO> reportReasonDTOs = postReportReasonListToReportReasonDTOsSet(upr.getReasons());
            finalResult.addAll(reportReasonDTOs);
        }

        return finalResult;
    }

    default Set<ReportReasonDTO> userPostCommentReportToReportReasonDTO (List<UserPostCommentReport> userPostCommentReportList) {
        Set<ReportReasonDTO> finalResult = new HashSet<>();

        for (UserPostCommentReport upcr : userPostCommentReportList) {
            Set<ReportReasonDTO> reportReasonDTOs = postCommentReportReasonListToReportReasonDTOsSet(upcr.getReasons());
            finalResult.addAll(reportReasonDTOs);
        }

        return finalResult;
    }
}
