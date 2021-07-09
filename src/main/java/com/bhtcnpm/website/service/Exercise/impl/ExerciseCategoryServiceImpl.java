package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseCategoryMapper;
import com.bhtcnpm.website.model.dto.ExerciseCategory.ExerciseCategoryDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseCategory;
import com.bhtcnpm.website.repository.Exercise.ExerciseCategoryRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseCategoryServiceImpl implements ExerciseCategoryService {

    private final ExerciseCategoryRepository exerciseCategoryRepository;
    private final ExerciseCategoryMapper exerciseCategoryMapper;

    @Override
    public List<ExerciseCategoryDTO> getExerciseCategories() {

        List<ExerciseCategory> exerciseCategoryDTOList = exerciseCategoryRepository.findAll();

        return exerciseCategoryMapper.exerciseCategoryListToExerciseCategoryDTOList(exerciseCategoryDTOList);
    }
}
