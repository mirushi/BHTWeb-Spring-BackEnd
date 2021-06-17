package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseNoteDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseNoteRequestDTO;
import com.bhtcnpm.website.service.Exercise.ExerciseNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
public class ExerciseNoteController {

    private final ExerciseNoteService exerciseNoteService;

    @GetMapping("/exercises/{exerciseID}/notes")
    @ResponseBody
    public ResponseEntity<ExerciseNoteDTO> getExerciseNote (@PathVariable("exerciseID") Long exerciseID,
                                                            Authentication authentication) {
        ExerciseNoteDTO exerciseNoteDTO = exerciseNoteService.getNoteByExerciseIDAndUser(exerciseID, authentication);

        return new ResponseEntity<>(exerciseNoteDTO, HttpStatus.OK);
    }

    @PutMapping("/exercises/{exerciseID}/notes")
    @ResponseBody
    public ResponseEntity<ExerciseNoteDTO> putExerciseNote (@PathVariable("exerciseID") Long exerciseID,
                                                            @RequestBody ExerciseNoteRequestDTO requestDTO,
                                                            Authentication authentication) {
        ExerciseNoteDTO exerciseNoteDTO = exerciseNoteService.putNoteByExerciseIDAndUser(exerciseID, requestDTO, authentication);

        return new ResponseEntity<>(exerciseNoteDTO, HttpStatus.OK);
    }

}
