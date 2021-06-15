package com.bhtcnpm.website.model.dto.Exercise;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class ExerciseDetailsDTO {
    private Long id;
    private String title;
    private String description;
    private Integer totalQuestions;
    private Integer suggestedDuration;
    private Set<TagDTO> tags;
    private Long categoryID;
    private String categoryName;
    private Long subjectID;
    private String subjectName;
    private UUID authorID;
    private String authorDisplayName;
    private String authorAvatarURL;
    private LocalDateTime publishDtm;
}
