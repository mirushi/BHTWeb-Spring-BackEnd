package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ExerciseQuestionRepository extends JpaRepository<ExerciseQuestion, Long> {

    long countAllByExerciseId (Long exerciseId);

    @EntityGraph(value = "answers.all")
    List<ExerciseQuestion> findAllByExerciseIdOrderByRankAsc (Long exerciseID);

    @EntityGraph(value = "answers.correct")
    List<ExerciseQuestion> findAllByIdIn (Set<Long> questionIDs);
}
