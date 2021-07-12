package com.bhtcnpm.website.model.dto.ExerciseCommentReport;

import com.bhtcnpm.website.model.entity.enumeration.ExerciseCommentReport.ExerciseCommentReportActionType;
import lombok.Value;

@Value
public class ExerciseCommentReportResolveRequestDTO {
    ExerciseCommentReportActionType exerciseCommentReportActionType;
    String resolvedNote;
}
