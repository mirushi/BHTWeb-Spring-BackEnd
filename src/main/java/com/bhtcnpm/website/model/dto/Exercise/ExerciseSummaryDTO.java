package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseSummaryDTO {
    private Long id;
    private String title;
    private Boolean attempted;
    private String description;
}
