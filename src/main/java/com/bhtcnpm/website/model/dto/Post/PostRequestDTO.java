package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.validator.dto.Post.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class PostRequestDTO {
    @PostTitle
    private String title;

    @PostContent
    private String content;

    @PostSummary
    private String summary;

    @PostImageURL
    private String imageURL;

    @PostCategoryID
    private Long categoryID;

    private LocalDateTime publishDtm;

    @PostTag
    private Set<TagDTO> tags;
}
