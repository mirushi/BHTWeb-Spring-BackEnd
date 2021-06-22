package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ExerciseSubjectRepository extends JpaRepository<ExerciseSubject, Long>, QuerydslPredicateExecutor<ExerciseSubject> {
}
