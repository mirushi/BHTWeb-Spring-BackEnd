package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuickSearchResult;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.model.dto.Exercise.filter.ExerciseSearchFilterRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseSearchMapper;
import com.bhtcnpm.website.model.dto.Exercise.sort.ExerciseSearchSortRequestDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.repository.Exercise.custom.ExerciseSearchRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseSearchServiceImpl implements ExerciseSearchService {

    private final ExerciseSearchRepository exerciseSearchRepository;
    private final ExerciseSearchMapper exerciseSearchMapper;
    private static final int PAGE_SIZE = 10;

    @Override
    public ExerciseSearchResultDTOList searchExercise(ExerciseSearchFilterRequestDTO filterRequestDTO, ExerciseSearchSortRequestDTO sortRequestDTO, Integer page, Authentication authentication) {
        Page<Exercise> searchResult = exerciseSearchRepository.searchPublicExercise(filterRequestDTO, sortRequestDTO, page, PAGE_SIZE, authentication);

        return exerciseSearchMapper.exercisePageToExerciseSearchResultDTOList(searchResult);
    }

    @Override
    public List<ExerciseQuickSearchResult> quickSearch(Pageable pageable, String searchTerm) {
        ExerciseSearchFilterRequestDTO dto = new ExerciseSearchFilterRequestDTO();
        dto.setSearchTerm(searchTerm);
        Page<Exercise> searchResult = exerciseSearchRepository.searchPublicExercise(dto, null, pageable.getPageNumber(), pageable.getPageSize(), null);

        return exerciseSearchMapper.exerciseListToExerciseQuickSearchResultList(searchResult.getContent());
    }
}
