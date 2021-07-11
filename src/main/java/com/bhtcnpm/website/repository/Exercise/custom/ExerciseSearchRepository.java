package com.bhtcnpm.website.repository.Exercise.custom;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseSearchRepository {
    Page<Exercise> searchPublicExercise (String searchTerm, Long categoryID, Long subjectID, UUID authorID, Long tagID, Integer page, ApiSortOrder sortByPublishDtm, ApiSortOrder sortByAttempts);
}
