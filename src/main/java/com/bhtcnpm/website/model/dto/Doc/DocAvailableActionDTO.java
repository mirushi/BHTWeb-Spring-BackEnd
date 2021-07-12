package com.bhtcnpm.website.model.dto.Doc;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DocAvailableActionDTO {
    private Long id;
    private List<String> availableActions;
}
