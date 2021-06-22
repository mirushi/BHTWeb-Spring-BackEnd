package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseNoteDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseNoteRequestDTO;
import org.springframework.security.core.Authentication;

public interface ExerciseNoteService {
    ExerciseNoteDTO getNoteByExerciseIDAndUser (Long exerciseID, Authentication authentication);
    ExerciseNoteDTO putNoteByExerciseIDAndUser (Long exerciseID, ExerciseNoteRequestDTO requestDTO, Authentication authentication);
}
