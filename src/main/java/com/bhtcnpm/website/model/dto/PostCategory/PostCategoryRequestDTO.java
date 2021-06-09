package com.bhtcnpm.website.model.dto.PostCategory;

import com.bhtcnpm.website.model.validator.dto.Post.PostCategoryName;
import lombok.Data;

@Data
public class PostCategoryRequestDTO {
    @PostCategoryName
    private String name;
}
