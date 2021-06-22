package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserWebsiteAvailableActionDTO {
    private UUID id;
    private List<String> availableActions;
}
