package com.bhtcnpm.website.model.dto.ExerciseComment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ExerciseCommentAvailableActionDTO {
    private Long id;
    private List<String> availableActions;
}
