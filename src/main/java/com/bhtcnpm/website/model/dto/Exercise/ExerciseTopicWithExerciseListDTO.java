package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseTopicWithExerciseListDTO {
    private Long id;
    private String name;
    private List<ExerciseSummaryDTO> exerciseSummaryDTOs;
}
