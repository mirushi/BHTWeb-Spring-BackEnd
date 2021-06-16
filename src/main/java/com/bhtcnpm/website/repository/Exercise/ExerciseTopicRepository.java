package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ExerciseTopicRepository extends JpaRepository<ExerciseTopic, Long>, QuerydslPredicateExecutor<ExerciseTopic> {
    List<ExerciseTopic> findAllBySubjectIdOrderByRankAsc (Long subjectID);
}
