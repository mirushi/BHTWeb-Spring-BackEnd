package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

import java.util.List;

//This is a cheat for speed-development of front-end.
//This unusual DTO will increase coupling between backend and frontend.
//Move the merging result operation to front-end.
//TODO: Please refactor this when you have time.
@Data
public class ExerciseSubjectSummaryWithTopicAndExercisesDTO {
    private Long id;
    private String name;
    private List<ExerciseTopicWithExerciseListDTO> exerciseTopicWithExerciseListDTOs;
}
