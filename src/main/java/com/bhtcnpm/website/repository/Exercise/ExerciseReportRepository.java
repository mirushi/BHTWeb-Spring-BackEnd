package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseReportRepository extends JpaRepository<ExerciseReport, Long> {
    @EntityGraph(value = "exerciseReport.all")
    Page<ExerciseReport> findAll(Pageable pageable);

    @EntityGraph(value = "exerciseReport.all")
    Page<ExerciseReport> findAllByResolvedTimeNotNull (Pageable pageable);

    @EntityGraph(value = "exerciseReport.all")
    Page<ExerciseReport> findAllByResolvedTimeIsNull (Pageable pageable);

    ExerciseReport findByExercise (Exercise exercise);

    ExerciseReport findByExerciseAndActionTakenIsNull (Exercise exercise);
}
