package com.bhtcnpm.website.repository.Exercise.custom;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.filter.ExerciseSearchFilterRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.sort.ExerciseSearchSortRequestDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseSearchRepository {
    Page<Exercise> searchPublicExercise (ExerciseSearchFilterRequestDTO filterRequestDTO, ExerciseSearchSortRequestDTO sortRequestDTO, Integer page, Integer pageSize, Authentication authentication);
}
