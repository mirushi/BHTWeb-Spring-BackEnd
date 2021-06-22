package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseNote;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseNoteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExerciseNoteRepository extends JpaRepository<ExerciseNote, ExerciseNoteId> {
    Optional<ExerciseNote> findByExerciseNoteIdExerciseIdAndExerciseNoteIdUserId (Long exerciseID, UUID userID);
}
