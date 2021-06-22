package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.entity.Tag;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostAvailableActionDTO {
    private Long id;
    private List<String> availableActions;
}
