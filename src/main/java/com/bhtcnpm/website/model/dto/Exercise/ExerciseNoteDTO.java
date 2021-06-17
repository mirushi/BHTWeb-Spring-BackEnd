package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExerciseNoteDTO {
    private String note;
    public ExerciseNoteDTO (String note) {
        this.note = note;
    }
}
