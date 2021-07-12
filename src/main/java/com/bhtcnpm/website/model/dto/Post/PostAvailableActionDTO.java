package com.bhtcnpm.website.model.dto.Post;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostAvailableActionDTO {
    private Long id;
    private List<String> availableActions;
}
