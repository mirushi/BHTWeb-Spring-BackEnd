package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectSummaryWithTopicAndExercisesDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicWithExerciseListDTO;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseSubjectMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubject;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseSubjectRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseSubjectService;
import com.bhtcnpm.website.service.Exercise.ExerciseTopicService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseSubjectServiceImpl implements ExerciseSubjectService {

    private final ExerciseSubjectRepository exerciseSubjectRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseTopicService exerciseTopicService;

    private final ExerciseSubjectMapper exerciseSubjectMapper;

    @Override
    public List<ExerciseSubjectSummaryDTO> getExerciseSubject(Predicate predicate) {
        Iterable<ExerciseSubject> exerciseSubjectSummaryDTOList = exerciseSubjectRepository.findAll(predicate);

        return exerciseSubjectMapper.exerciseSubjectIterableToExerciseSubjectSummaryDTOList(exerciseSubjectSummaryDTOList);
    }

    @Override
    public ExerciseSubjectSummaryWithTopicAndExercisesDTO getExerciseSubjectWithTopicAndExercises(Long exerciseID) {
        Optional<Exercise> object = exerciseRepository.findById(exerciseID);

        if (object.isEmpty()) {
            throw new IDNotFoundException();
        }

        Exercise entity = object.get();
        ExerciseSubjectSummaryWithTopicAndExercisesDTO dto = new ExerciseSubjectSummaryWithTopicAndExercisesDTO();
        dto.setId(entity.getTopic().getSubject().getId());
        dto.setName(entity.getTopic().getSubject().getName());

        List<ExerciseTopicWithExerciseListDTO> dtoList = exerciseTopicService.getAllExerciseTopicsWithExerciseList(entity.getTopic().getSubject().getId(), null);
        dto.setExerciseTopicWithExerciseListDTOs(dtoList);

        return dto;
    }
}
