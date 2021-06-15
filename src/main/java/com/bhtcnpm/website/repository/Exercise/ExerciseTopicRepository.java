package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseTopicRepository extends JpaRepository<ExerciseTopic, Long> {
}
