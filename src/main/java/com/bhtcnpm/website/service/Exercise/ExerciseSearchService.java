package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;

import java.util.List;
import java.util.UUID;

public interface ExerciseSearchService {
    ExerciseSearchResultDTOList searchExercise (String searchTerm, Long categoryID, Long subjectID, UUID authorID, Long tagID, Integer page, ApiSortOrder sortByPublishDtm, ApiSortOrder sortByAttempts);
}
