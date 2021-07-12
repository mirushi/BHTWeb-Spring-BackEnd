package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.report.UserExerciseCommentReport;
import com.bhtcnpm.website.model.entity.ExerciseEntities.report.UserExerciseCommentReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExerciseCommentReportRepository extends JpaRepository<UserExerciseCommentReport, UserExerciseCommentReportId> {
}
