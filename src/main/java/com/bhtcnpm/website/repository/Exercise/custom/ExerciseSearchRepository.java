package com.bhtcnpm.website.repository.Exercise.custom;

import com.bhtcnpm.website.constant.domain.Exercise.ExerciseBusinessState;
import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.filter.ExerciseSearchFilterRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.sort.ExerciseSearchSortRequestDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseSearchRepository {
    Page<Exercise> searchPublicExercise (ExerciseSearchFilterRequestDTO filterRequestDTO, ExerciseSearchSortRequestDTO sortRequestDTO, Integer page, Integer pageSize, Authentication authentication);
    Page<Exercise> getRelatedExercise (String title, String description, int page, int pageSize, ExerciseBusinessState exerciseBusinessState) throws IOException;
}
