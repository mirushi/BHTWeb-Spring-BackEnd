package com.bhtcnpm.website.model.dto.UserExerciseReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseReportAction.ExerciseReportActionType;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Value
@AllArgsConstructor
public class ExerciseReportDTO {
    Long id;
    Long exerciseID;
    String title;
    String description;
    Integer suggestedDuration;
    Long categoryID;
    String categoryName;
    Long topicID;
    String topicName;
    Long subjectID;
    String subjectName;
    UserSummaryDTO author;
    List<UserSummaryDTO> reporters;
    Set<ReportReasonDTO> reportReasons;
    List<String> feedbacks;
    LocalDateTime reportTime;
    LocalDateTime resolvedTime;
    String resolvedNote;
    UserSummaryDTO resolvedBy;
    ExerciseReportActionType actionTaken;
}
