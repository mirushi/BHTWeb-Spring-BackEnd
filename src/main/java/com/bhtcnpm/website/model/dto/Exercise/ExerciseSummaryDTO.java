package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExerciseSummaryDTO {
    private Long id;
    private String title;
    private String description;
    private Boolean attempted;

    public ExerciseSummaryDTO (Long id, String title, String description, Boolean attempted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.attempted = attempted;
    }
}
