package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import lombok.Data;

@Data
public class DocSuggestionDTO {
    private Long id;
    private String title;
    private String description;
    private UserSummaryDTO author;
}
