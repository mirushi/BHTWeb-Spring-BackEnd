package com.bhtcnpm.website.model.dto.Exercise.filter;

import lombok.Data;

import java.util.UUID;

@Data
public class ExerciseSearchFilterRequestDTO {
    private String searchTerm;
    private Long categoryID;
    private Long subjectID;
    private UUID authorID;
    private Long tagID;
}
