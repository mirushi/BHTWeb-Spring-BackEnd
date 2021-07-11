package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExerciseSearchResultDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime publishDtm;
}
