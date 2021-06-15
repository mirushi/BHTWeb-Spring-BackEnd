package com.bhtcnpm.website.repository.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseNote;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseNoteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseNoteRepository extends JpaRepository<ExerciseNote, ExerciseNoteId> {
}
