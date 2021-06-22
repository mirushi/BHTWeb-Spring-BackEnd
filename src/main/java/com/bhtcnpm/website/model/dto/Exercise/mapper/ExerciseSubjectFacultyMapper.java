package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectFacultySummaryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectFaculty;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ExerciseSubjectFacultyMapper {
    ExerciseSubjectFacultySummaryDTO exerciseSubjectFacultyToExerciseSubjectFacultySummaryDTO (ExerciseSubjectFaculty exerciseSubjectFaculty);

    List<ExerciseSubjectFacultySummaryDTO> exerciseSubjectFacultyListToExerciseSubjectFacultySummaryDTOList (List<ExerciseSubjectFaculty> exerciseSubjectFacultyList);
}
