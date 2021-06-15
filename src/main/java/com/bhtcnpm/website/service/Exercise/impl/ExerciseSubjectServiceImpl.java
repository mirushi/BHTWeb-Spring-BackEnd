package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseSubjectMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubject;
import com.bhtcnpm.website.repository.Exercise.ExerciseSubjectRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseSubjectService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseSubjectServiceImpl implements ExerciseSubjectService {

    private final ExerciseSubjectRepository exerciseSubjectRepository;
    private final ExerciseSubjectMapper exerciseSubjectMapper;

    @Override
    public List<ExerciseSubjectSummaryDTO> getExerciseSubject(Predicate predicate) {
        Iterable<ExerciseSubject> exerciseSubjectSummaryDTOList = exerciseSubjectRepository.findAll(predicate);

        return exerciseSubjectMapper.exerciseSubjectIterableToExerciseSubjectSummaryDTOList(exerciseSubjectSummaryDTOList);
    }
}
