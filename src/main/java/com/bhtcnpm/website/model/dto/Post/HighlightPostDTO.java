package com.bhtcnpm.website.model.dto.Post;

import lombok.Data;

@Data
public class HighlightPostDTO {
    private PostSummaryDTO postSummaryDTO;
    private Integer rank;
}
