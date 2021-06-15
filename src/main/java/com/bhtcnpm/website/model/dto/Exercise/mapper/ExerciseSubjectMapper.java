package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectSummaryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubject;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ExerciseSubjectMapper {
    ExerciseSubjectSummaryDTO exerciseSubjectToExerciseSubjectSummaryDTO(ExerciseSubject exerciseSubject);

    List<ExerciseSubjectSummaryDTO> exerciseSubjectIterableToExerciseSubjectSummaryDTOList (Iterable<ExerciseSubject> exerciseSubjectIterable);
}
