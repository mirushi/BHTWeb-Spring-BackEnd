package com.bhtcnpm.website.model.dto.PostCategory;

import com.bhtcnpm.website.model.validator.dto.Post.PostCategoryID;
import com.bhtcnpm.website.model.validator.dto.PostCategory.PostCategoryName;
import lombok.Data;

@Data
public class PostCategoryDTO {
    @PostCategoryID
    private Long id;
    @PostCategoryName
    private String name;
}
