package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public abstract class ExerciseSearchMapper {
    public abstract ExerciseSearchResultDTO exerciseToExerciseSearchResultDTO (Exercise exercise);
    public abstract List<ExerciseSearchResultDTO> exerciseListToExerciseSearchResultDTOList (List<Exercise> exerciseList);

    public ExerciseSearchResultDTOList exercisePageToExerciseSearchResultDTOList (Page<Exercise> exercisePage) {
        ExerciseSearchResultDTOList result = new ExerciseSearchResultDTOList();

        result.setExerciseSearchResultDTOs(this.exerciseListToExerciseSearchResultDTOList(exercisePage.getContent()));
        result.setTotalPages(exercisePage.getTotalPages());
        result.setTotalElements(exercisePage.getTotalElements());

        return result;
    }
}
