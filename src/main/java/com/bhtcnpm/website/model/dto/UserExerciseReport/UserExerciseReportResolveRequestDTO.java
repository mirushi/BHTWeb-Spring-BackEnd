package com.bhtcnpm.website.model.dto.UserExerciseReport;

import com.bhtcnpm.website.model.entity.enumeration.ExerciseReportAction.ExerciseReportActionType;
import lombok.Value;

@Value
public class UserExerciseReportResolveRequestDTO {
    ExerciseReportActionType exerciseReportActionType;
    String resolvedNote;
}
