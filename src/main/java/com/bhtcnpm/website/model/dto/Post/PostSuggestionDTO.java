package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import lombok.Data;

@Data
public class PostSuggestionDTO {
    private Long id;
    private String title;
    private String summary;
    private UserSummaryDTO author;
}
