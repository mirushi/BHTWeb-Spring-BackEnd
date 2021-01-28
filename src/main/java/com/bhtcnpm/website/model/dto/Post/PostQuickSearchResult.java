package com.bhtcnpm.website.model.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostQuickSearchResult {
    private Long id;
    private String imageURL;
    private String title;
}
