package com.bhtcnpm.website.model.dto.Exercise;

import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import lombok.Data;

@Data
public class ExerciseSuggestionDTO {
    private Long id;
    private String title;
    private String description;
    private UserSummaryDTO author;
}
