package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseTopic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ExerciseTopicMapper {
    ExerciseTopicDTO exerciseTopicToExerciseTopicDTO (ExerciseTopic exerciseTopic);

    List<ExerciseTopicDTO> exerciseTopicListToExerciseTopicDTOList (List<ExerciseTopic> exerciseTopicList);

    List<ExerciseTopicDTO> exerciseTopicIterableToExerciseTopicDTOList (Iterable<ExerciseTopic> exerciseTopicIterable);
}
