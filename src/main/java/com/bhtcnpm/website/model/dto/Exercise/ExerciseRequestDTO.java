package com.bhtcnpm.website.model.dto.Exercise;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.entity.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ExerciseRequestDTO {
    private String title;
    private String description;
    private Long categoryID;
    private Long topicID;
    private Integer suggestedDuration;
    private LocalDateTime publishDtm;
    private Integer rank;
    private Set<TagDTO> tags;
}
