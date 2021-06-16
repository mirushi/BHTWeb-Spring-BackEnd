package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ExerciseMapper {
    @Mapping(target = "attempted", constant = "false")
    ExerciseSummaryDTO exerciseToExerciseSummaryDTO (Exercise exercise);

    @Mapping(target = "attempted", constant = "false")
    @Mapping(target = "topicID", source = "topic.id")
    @Mapping(target = "topicName", source = "topic.name")
    ExerciseSummaryWithTopicDTO exerciseToExerciseSummaryWithTopicDTO (Exercise exercise);

    List<ExerciseSummaryWithTopicDTO> exerciseIterableToExerciseSummaryWithTopicDTOList (Iterable<Exercise> exerciseIterable);

    List<ExerciseSummaryWithTopicDTO> exerciseListToExerciseSummaryWithTopicDTOList (List<Exercise> exerciseList);

    List<ExerciseSummaryDTO> exerciseIterableToExerciseSummaryDTOList (Iterable<Exercise> exerciseIterable);

    List<ExerciseSummaryDTO> exerciseListToExerciseSummaryDTOList (List<Exercise> exerciseList);

    ExerciseSummaryDTO exerciseWithTopicToExerciseSummaryDTOList (ExerciseSummaryWithTopicDTO exerciseSummaryWithTopicDTO);
}
