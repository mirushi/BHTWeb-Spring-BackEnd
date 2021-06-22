package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicWithExerciseListDTO;
import com.querydsl.core.types.Predicate;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseTopicService {
    List<ExerciseTopicDTO> getAllExerciseTopicsBySubject (Predicate predicate);
    List<ExerciseTopicWithExerciseListDTO> getAllExerciseTopicsWithExerciseList (Long subjectID, Authentication authentication);
}
