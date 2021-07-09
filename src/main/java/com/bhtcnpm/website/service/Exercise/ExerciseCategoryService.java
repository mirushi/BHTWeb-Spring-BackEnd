package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseCategory.ExerciseCategoryDTO;

import java.util.List;

public interface ExerciseCategoryService {
    List<ExerciseCategoryDTO> getExerciseCategories();
}
