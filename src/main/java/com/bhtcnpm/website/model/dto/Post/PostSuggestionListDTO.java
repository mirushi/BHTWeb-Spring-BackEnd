package com.bhtcnpm.website.model.dto.Post;

import lombok.Data;

@Data
public class PostSuggestionListDTO {
    private PostSuggestionDTO postSuggestionDTO;
    private Integer totalPages;
    private Long totalElements;
}
