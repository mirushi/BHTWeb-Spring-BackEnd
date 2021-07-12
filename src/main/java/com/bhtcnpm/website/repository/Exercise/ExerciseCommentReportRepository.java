package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseComment;
import com.bhtcnpm.website.model.entity.ExerciseEntities.report.ExerciseCommentReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseCommentReportRepository extends JpaRepository<ExerciseCommentReport, Long> {
    @EntityGraph("exerciseCommentReport.all")
    Page<ExerciseCommentReport> findAll (Pageable pageable);

    @EntityGraph("exerciseCommentReport.all")
    Page<ExerciseCommentReport> findAllByResolvedTimeNotNull (Pageable pageable);

    @EntityGraph("exerciseCommentReport.all")
    Page<ExerciseCommentReport> findAllByResolvedTimeIsNull (Pageable pageable);

    ExerciseCommentReport findByExerciseComment (ExerciseComment exerciseComment);

    ExerciseCommentReport findByExerciseCommentAndActionTakenIsNull (ExerciseComment exerciseComment);
}
