package com.bhtcnpm.website.model.dto.ExerciseCommentReport;

import lombok.Value;

import java.util.List;

@Value
public class ExerciseCommentReportRequestDTO {
    List<Long> reasonIds;
    String feedback;
}
