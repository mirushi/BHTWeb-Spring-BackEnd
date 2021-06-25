package com.bhtcnpm.website.model.dto.ExerciseComment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExerciseCommentStatisticDTO {
    private Long id;
    private Long likeCount;
    private Boolean likeStatus;
}
