package com.bhtcnpm.website.service.Exercise;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuickSearchResult;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.model.dto.Exercise.filter.ExerciseSearchFilterRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.sort.ExerciseSearchSortRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseSearchService {
    ExerciseSearchResultDTOList searchExercise (ExerciseSearchFilterRequestDTO filterRequestDTO, ExerciseSearchSortRequestDTO sortRequestDTO, Integer page, Authentication authentication);
    List<ExerciseQuickSearchResult> quickSearch (Pageable pageable, String searchTerm);
}
