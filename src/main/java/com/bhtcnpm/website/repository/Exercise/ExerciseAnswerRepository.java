package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ExerciseAnswerRepository extends JpaRepository<ExerciseAnswer, Long>, QuerydslPredicateExecutor<ExerciseAnswer> {
    List<ExerciseAnswer> findAllByQuestionId (Long questionID);
}
