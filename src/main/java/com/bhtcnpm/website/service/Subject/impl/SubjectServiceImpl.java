package com.bhtcnpm.website.service.Subject.impl;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicWithExerciseListDTO;
import com.bhtcnpm.website.model.dto.Subject.SubjectRequestDTO;
import com.bhtcnpm.website.model.dto.Subject.SubjectSummaryDTO;
import com.bhtcnpm.website.model.dto.Subject.SubjectSummaryWithTopicAndExercisesDTO;
import com.bhtcnpm.website.model.dto.Subject.mapper.SubjectMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.entity.SubjectEntities.Subject;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.Subject.SubjectRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseTopicService;
import com.bhtcnpm.website.service.Subject.SubjectService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseTopicService exerciseTopicService;
    private final SubjectMapper subjectMapper;

    @Override
    public List<SubjectSummaryDTO> getSubjects(Predicate predicate) {
        Iterable<Subject> queryResult = subjectRepository.findAll(predicate);
        return subjectMapper.subjectIterableToSubjectSummaryDTOList(queryResult);
    }

    @Override
    public SubjectSummaryDTO createSubject(SubjectRequestDTO requestDTO) {
        Subject subject = subjectMapper.subjectDTORequestToSubject(requestDTO, new Subject());
        subject = subjectRepository.save(subject);
        return subjectMapper.subjectToSubjectSummaryDTO(subject);
    }

    @Override
    public SubjectSummaryDTO updateSubject(Long subjectID, SubjectRequestDTO requestDTO) {
        Subject subject = subjectRepository.getOne(subjectID);
        subject = subjectMapper.subjectDTORequestToSubject(requestDTO, subject);
        subject = subjectRepository.save(subject);
        return subjectMapper.subjectToSubjectSummaryDTO(subject);
    }

    @Override
    public Boolean deleteSubject(Long subjectID) {
        subjectRepository.deleteById(subjectID);
        return true;
    }

    @Override
    public SubjectSummaryWithTopicAndExercisesDTO getSubjectWithTopicAndExercises(Long exerciseID) {
        Optional<Exercise> object = exerciseRepository.findById(exerciseID);

        if (object.isEmpty()) {
            throw new IDNotFoundException();
        }

        Exercise entity = object.get();
        List<ExerciseTopicWithExerciseListDTO> dtoList = exerciseTopicService.getAllExerciseTopicsWithExerciseList(entity.getTopic().getSubject().getId(), null);
        SubjectSummaryWithTopicAndExercisesDTO dto = SubjectSummaryWithTopicAndExercisesDTO.builder()
                .id(entity.getTopic().getSubject().getId())
                .name(entity.getTopic().getSubject().getName())
                .exerciseTopicWithExerciseListDTOs(dtoList).build();

        return dto;
    }
}
