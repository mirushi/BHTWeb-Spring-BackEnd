package com.bhtcnpm.website.model.dto.UserExerciseReport;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseReport;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserExerciseReportListDTO {
    List<ExerciseReportDTO> exerciseReportDTOs;
    Long totalElements;
    Integer totalPages;
}