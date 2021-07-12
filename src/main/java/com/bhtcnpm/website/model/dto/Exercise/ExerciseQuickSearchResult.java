package com.bhtcnpm.website.model.dto.Exercise;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExerciseQuickSearchResult {
    private Long id;
    private String imageURL;
    private String title;
}
