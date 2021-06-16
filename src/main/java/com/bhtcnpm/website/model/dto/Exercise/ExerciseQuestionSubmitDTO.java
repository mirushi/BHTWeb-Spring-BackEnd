package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseQuestionSubmitDTO {
    private Long id;
    private List<Long> answersSelected;
}
