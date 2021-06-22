package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectGroupSummaryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectGroup;
import com.bhtcnpm.website.service.Exercise.ExerciseSubjectGroupService;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ExerciseSubjectGroupMapper {

    ExerciseSubjectGroupSummaryDTO exerciseSubjectGroupToExerciseSubjectGroupSummary (ExerciseSubjectGroup exerciseSubjectGroup);

    List<ExerciseSubjectGroupSummaryDTO> exerciseSubjectGroupListToExerciseSubjectGroupSummaryList (List<ExerciseSubjectGroup> exerciseSubjectGroupList);

}
