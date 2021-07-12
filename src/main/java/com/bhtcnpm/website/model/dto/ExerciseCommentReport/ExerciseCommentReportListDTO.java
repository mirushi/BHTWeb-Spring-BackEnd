package com.bhtcnpm.website.model.dto.ExerciseCommentReport;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ExerciseCommentReportListDTO {
    List<ExerciseCommentReportDTO> exerciseCommentReportDTOs;
    Long totalElements;
    Integer totalPages;
}
