package com.bhtcnpm.website.model.dto.UserExerciseReport;

import lombok.Value;

import java.util.List;

@Value
public class UserExerciseReportRequestDTO {
    List<Long> reasonIDs;
    String feedback;
}
