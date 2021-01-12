package com.bhtcnpm.website.model.dto.Tag;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TagDTO {
    private Long id;

    @NotBlank
    private String content;
}
