package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.querydsl.core.types.Predicate;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseService {
    List<ExerciseSummaryDTO> getExerciseList (Predicate predicate, Authentication authentication);
}
