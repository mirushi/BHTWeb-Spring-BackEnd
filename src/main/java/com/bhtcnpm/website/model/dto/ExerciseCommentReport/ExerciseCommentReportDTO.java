package com.bhtcnpm.website.model.dto.ExerciseCommentReport;

import com.bhtcnpm.website.model.dto.Doc.DocFileUploadDTO;
import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonDTO;
import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import com.bhtcnpm.website.model.entity.enumeration.DocReport.DocReportActionType;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseCommentReport.ExerciseCommentReportActionType;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseReportAction.ExerciseReportActionType;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Value
public class ExerciseCommentReportDTO {
    Long id;
    Long commentID;
    String content;
    Long exerciseID;
    String exerciseTitle;
    String description;
    UserSummaryDTO author;
    LocalDateTime submitDtm;
    Long categoryID;
    String categoryName;
    Long topicID;
    String topicName;
    Long subjectID;
    String subjectName;
    Set<TagDTO> tags;
    List<UserSummaryDTO> reporters;
    Set<ReportReasonDTO> reportReasons;
    List<String> feedbacks;
    LocalDateTime reportTime;
    LocalDateTime resolvedTime;
    String resolvedNote;
    UserSummaryDTO resolvedBy;
    ExerciseCommentReportActionType actionTaken;
}
