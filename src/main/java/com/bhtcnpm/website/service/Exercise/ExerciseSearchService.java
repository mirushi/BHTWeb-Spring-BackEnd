package com.bhtcnpm.website.service.Exercise;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.model.dto.Exercise.filter.ExerciseSearchFilterRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.sort.ExerciseSearchSortRequestDTO;
import org.springframework.security.core.Authentication;

public interface ExerciseSearchService {
    ExerciseSearchResultDTOList searchExercise (ExerciseSearchFilterRequestDTO filterRequestDTO, ExerciseSearchSortRequestDTO sortRequestDTO, Integer page, Authentication authentication);
}
