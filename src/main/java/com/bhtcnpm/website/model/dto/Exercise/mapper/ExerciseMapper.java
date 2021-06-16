package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseDetailsDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseUserStatisticDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ExerciseMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "bestCorrectQuestions", ignore = true)
    @Mapping(target = "notes", ignore = true)
    ExerciseUserStatisticDTO exerciseIDToExerciseUserStatisticDTO (Long id);

    @Mapping(target = "attempted", constant = "false")
    ExerciseSummaryDTO exerciseToExerciseSummaryDTO (Exercise exercise);

    @Mapping(target = "attempted", constant = "false")
    @Mapping(target = "topicID", source = "topic.id")
    @Mapping(target = "topicName", source = "topic.name")
    ExerciseSummaryWithTopicDTO exerciseToExerciseSummaryWithTopicDTO (Exercise exercise);

    ExerciseSummaryDTO exerciseWithTopicToExerciseSummaryDTOList (ExerciseSummaryWithTopicDTO exerciseSummaryWithTopicDTO);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "topicID", source = "topic.id")
    @Mapping(target = "topicName", source = "topic.name")
    @Mapping(target = "subjectID", source = "topic.subject.id")
    @Mapping(target = "subjectName", source = "topic.subject.name")
    ExerciseDetailsDTO exerciseToExerciseDetailsDTO (Exercise exercise);

    List<ExerciseSummaryWithTopicDTO> exerciseIterableToExerciseSummaryWithTopicDTOList (Iterable<Exercise> exerciseIterable);

    List<ExerciseSummaryWithTopicDTO> exerciseListToExerciseSummaryWithTopicDTOList (List<Exercise> exerciseList);

    List<ExerciseSummaryDTO> exerciseIterableToExerciseSummaryDTOList (Iterable<Exercise> exerciseIterable);

    List<ExerciseSummaryDTO> exerciseListToExerciseSummaryDTOList (List<Exercise> exerciseList);

    List<ExerciseUserStatisticDTO> exerciseIDListToExerciseUserStatisticDTOList (List<Long> exerciseIDs);

}
