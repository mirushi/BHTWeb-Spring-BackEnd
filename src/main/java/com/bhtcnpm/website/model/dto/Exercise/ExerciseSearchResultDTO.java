package com.bhtcnpm.website.model.dto.Exercise;

import com.bhtcnpm.website.model.dto.Subject.SubjectSummaryDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExerciseSearchResultDTO {
    private Long id;
    private String title;
    private String description;
    private UserSummaryDTO author;
    private SubjectSummaryDTO subject;
    private ExerciseCategoryDTO category;
    private LocalDateTime publishDtm;
}
