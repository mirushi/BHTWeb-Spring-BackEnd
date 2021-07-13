package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuickSearchResult;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.model.dto.Subject.mapper.SubjectMapper;
import com.bhtcnpm.website.model.dto.UserWebsite.UserMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(uses = {SubjectMapper.class, ExerciseCategoryMapper.class, UserMapper.class})
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

    @Mapping(target = "imageURL", source = "subject.imageURL")
    public abstract ExerciseQuickSearchResult exerciseToExerciseQuickSearchResult (Exercise exercise);
    public abstract List<ExerciseQuickSearchResult> exerciseListToExerciseQuickSearchResultList (List<Exercise> exerciseList);
}
