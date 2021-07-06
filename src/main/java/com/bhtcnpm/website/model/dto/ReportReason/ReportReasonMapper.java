package com.bhtcnpm.website.model.dto.ReportReason;

import com.bhtcnpm.website.model.entity.DocEntities.report.DocReport;
import com.bhtcnpm.website.model.entity.DocEntities.report.DocReportReason;
import com.bhtcnpm.website.model.entity.DocEntities.report.UserDocReport;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseReportReason;
import com.bhtcnpm.website.model.entity.ExerciseEntities.UserExerciseReport;
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

    @Mapping(target = "id", source = "exerciseReportReason.exerciseReportReasonId.reportReason.id")
    @Mapping(target = "reason", source = "exerciseReportReason.exerciseReportReasonId.reportReason.reason")
    ReportReasonDTO exerciseReportReasonToReportReasonDTO (ExerciseReportReason exerciseReportReason);

    Set<ReportReasonDTO> exerciseReportReasonListToReportReasonDTOsSet (List<ExerciseReportReason> exerciseReportReasons);

    @Mapping(target = "id", source = "docReportReason.docReportReasonId.reportReason.id")
    @Mapping(target = "reason", source = "docReportReason.docReportReasonId.reportReason.reason")
    ReportReasonDTO docReportReasonToReportReasonDTO (DocReportReason docReportReason);

    Set<ReportReasonDTO> docReportReasonListToReportReasonDTOsSet (List<DocReportReason> docReportReasons);

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

    default Set<ReportReasonDTO> userExerciseReportToReportReasonDTO (List<UserExerciseReport> userExerciseReportList) {
        Set<ReportReasonDTO> finalResult = new HashSet<>();

        for (UserExerciseReport uer : userExerciseReportList) {
            Set<ReportReasonDTO> reportReasonDTOs = exerciseReportReasonListToReportReasonDTOsSet(uer.getReasons());
            finalResult.addAll(reportReasonDTOs);
        }

        return finalResult;
    }

    default Set<ReportReasonDTO> docReportToReportReasonDTO (List<UserDocReport> docReportList) {
        Set<ReportReasonDTO> finalResult = new HashSet<>();

        for (UserDocReport dr : docReportList) {
            Set<ReportReasonDTO> reportReasonDTOs = docReportReasonListToReportReasonDTOsSet(dr.getReasons());
            finalResult.addAll(reportReasonDTOs);
        }

        return finalResult;
    }
}
