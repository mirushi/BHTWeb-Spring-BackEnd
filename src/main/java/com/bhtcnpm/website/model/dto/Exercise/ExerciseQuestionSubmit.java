package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseQuestionSubmit {
    private Long id;
    private List<Long> answersSelected;
}
