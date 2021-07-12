package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseAvailableActionDTO {
    private Long id;
    private List<String> availableActions;
}
