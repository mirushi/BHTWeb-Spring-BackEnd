package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import lombok.Data;

@Data
public class HighlightPostRequestDTO {
    @PostID
    private Long id;
}
