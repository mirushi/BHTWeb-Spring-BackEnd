package com.bhtcnpm.website.model.dto.Post;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class HighlightPostDTO {
    private PostSummaryDTO postSummaryDTO;

    private Integer rank;
}
