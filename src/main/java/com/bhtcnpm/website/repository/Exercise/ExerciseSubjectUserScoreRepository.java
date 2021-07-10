package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectUserScore;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectUserScoreId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExerciseSubjectUserScoreRepository extends JpaRepository<ExerciseSubjectUserScore, ExerciseSubjectUserScoreId> {
    List<ExerciseSubjectUserScore> findFirst10ByExerciseSubjectUserScoreIdSubjectIdOrderByTotalScoreDesc (Long subjectID);
}
