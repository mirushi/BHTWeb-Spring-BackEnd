package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectSummaryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubject;
import com.querydsl.core.types.Predicate;

import java.util.List;

public interface ExerciseSubjectService {
    List<ExerciseSubjectSummaryDTO> getExerciseSubject (Predicate predicate);
}
