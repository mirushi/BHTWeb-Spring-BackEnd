package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseSubjectGroupMapper;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectGroupSummaryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectGroup;
import com.bhtcnpm.website.repository.Exercise.ExerciseSubjectGroupRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseSubjectGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseSubjectGroupServiceImpl implements ExerciseSubjectGroupService {

    private final ExerciseSubjectGroupRepository exerciseSubjectGroupRepository;

    private final ExerciseSubjectGroupMapper exerciseSubjectGroupMapper;

    @Override
    public List<ExerciseSubjectGroupSummaryDTO> getExerciseSubjectGroup() {
        List<ExerciseSubjectGroup> exerciseSubjectGroupList = exerciseSubjectGroupRepository.findAll();

        return exerciseSubjectGroupMapper.exerciseSubjectGroupListToExerciseSubjectGroupSummaryList(exerciseSubjectGroupList);
    }
}
