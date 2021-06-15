package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSubjectFacultySummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseSubjectFacultyMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectFaculty;
import com.bhtcnpm.website.repository.Exercise.ExerciseSubjectFacultyRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseSubjectFacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseSubjectFacultyServiceImpl implements ExerciseSubjectFacultyService {
    private final ExerciseSubjectFacultyRepository exerciseSubjectFacultyRepository;
    private final ExerciseSubjectFacultyMapper exerciseSubjectFacultyMapper;
    @Override
    public List<ExerciseSubjectFacultySummaryDTO> getAllExerciseSubjectFaculty() {
        List<ExerciseSubjectFaculty> exerciseSubjectFacultyList = exerciseSubjectFacultyRepository.findAll();

        return exerciseSubjectFacultyMapper.exerciseSubjectFacultyListToExerciseSubjectFacultySummaryDTOList(exerciseSubjectFacultyList);
    }
}
