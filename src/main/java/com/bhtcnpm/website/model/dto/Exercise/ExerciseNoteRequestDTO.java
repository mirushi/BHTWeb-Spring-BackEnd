package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExerciseNoteRequestDTO {
    private String note;
    public ExerciseNoteRequestDTO (String note) {
        this.note = note;
    }
}
