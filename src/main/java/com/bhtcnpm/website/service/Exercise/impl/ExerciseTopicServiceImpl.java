package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseTopic;
import com.bhtcnpm.website.repository.Exercise.ExerciseTopicRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseTopicServiceImpl implements ExerciseTopicService {

    private final ExerciseTopicRepository exerciseTopicRepository;

    @Override
    public List<ExerciseTopicDTO> getAllExerciseTopicsBySubject(Long subjectID) {
        List<ExerciseTopic> exerciseTopicList = exerciseTopicRepository.findAllBySubjectId(subjectID);

        return null;
    }
}
