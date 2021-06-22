package com.bhtcnpm.website.model.dto.PostComment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostCommentAvailableActionDTO {
    private Long id;
    private List<String> availableActions;
}
