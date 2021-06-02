package com.bhtcnpm.website.model.dto.Post;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class HighlightPostRequestDTO {
    @Min(0)
    private Long id;
}
