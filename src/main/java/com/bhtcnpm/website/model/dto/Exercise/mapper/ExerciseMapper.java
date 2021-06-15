package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ExerciseMapper {
    ExerciseSummaryDTO exerciseToExerciseSummaryDTO (Exercise exercise);

    List<ExerciseSummaryDTO> exerciseIterableToExerciseSummaryDTOList (Iterable<Exercise> exerciseIterable);
}
