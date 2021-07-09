package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.ExerciseCategory.ExerciseCategoryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ExerciseCategoryMapper {
    ExerciseCategoryDTO exerciseCategoryToExerciseCategoryDTO (ExerciseCategory exerciseCategory);
    List<ExerciseCategoryDTO> exerciseCategoryListToExerciseCategoryDTOList (List<ExerciseCategory> exerciseCategoryList);
}
