package com.bhtcnpm.website.repository.Exercise.custom;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExerciseSearchRepositoryImpl implements ExerciseSearchRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Page<Exercise> searchPublicExercise(String searchTerm, Long categoryID, Long subjectID, UUID authorID, Long tagID, Integer page, ApiSortOrder sortByPublishDtm, ApiSortOrder sortByAttempts) {
        return null;
    }
}
