package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.UserExerciseReport;
import com.bhtcnpm.website.model.entity.ExerciseEntities.UserExerciseReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExerciseReportRepository extends JpaRepository<UserExerciseReport, UserExerciseReportId> {
    UserExerciseReport findByUserExerciseReportId (UserExerciseReportId id);
}
