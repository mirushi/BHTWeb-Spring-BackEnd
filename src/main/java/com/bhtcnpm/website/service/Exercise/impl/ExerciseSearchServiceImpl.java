package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.repository.Exercise.custom.ExerciseSearchRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseSearchServiceImpl implements ExerciseSearchService {

    private final ExerciseSearchRepository exerciseSearchRepository;

    @Override
    public ExerciseSearchResultDTOList searchExercise(String searchTerm, Long categoryID, Long subjectID, UUID authorID, Long tagID, Integer page, ApiSortOrder sortByPublishDtm, ApiSortOrder sortByAttempts) {
//        exerciseSearchRepository.searchPublicExercise(searchTerm, categoryID)
        return null;
    }
}
