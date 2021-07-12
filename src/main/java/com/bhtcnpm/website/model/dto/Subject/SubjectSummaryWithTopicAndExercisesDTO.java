package com.bhtcnpm.website.model.dto.Subject;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicWithExerciseListDTO;
import lombok.Builder;
import lombok.Value;

import java.util.List;

//This is a cheat for speed-development of front-end.
//This unusual DTO will increase coupling between backend and frontend.
//Move the merging result operation to front-end.
//TODO: Please refactor this when you have time.
@Value
@Builder
public class SubjectSummaryWithTopicAndExercisesDTO {
    Long id;
    String name;
    List<ExerciseTopicWithExerciseListDTO> exerciseTopicWithExerciseListDTOs;
}
