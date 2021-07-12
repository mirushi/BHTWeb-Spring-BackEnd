package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.ExerciseSubjectUserScoreDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectUserScore;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectUserScoreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExerciseSubjectUserScoreRepository extends JpaRepository<ExerciseSubjectUserScore, ExerciseSubjectUserScoreId> {
    List<ExerciseSubjectUserScore> findFirst10ByExerciseSubjectUserScoreIdSubjectIdOrderByTotalScoreDesc (Long subjectID);
    @Query("SELECT COUNT(exerciseSubjectUserScore.exerciseSubjectUserScoreId.user.id) " +
            "FROM ExerciseSubjectUserScore exerciseSubjectUserScore " +
            "WHERE exerciseSubjectUserScore.exerciseSubjectUserScoreId.subject.id = :subjectID AND exerciseSubjectUserScore.totalScore >= :userTotalScore")
    Long getUserSubjectRank (Long userTotalScore, Long subjectID);
}
