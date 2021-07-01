package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.validator.dto.Post.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class PostDetailsDTO {
    @PostID
    private Long id;

    @PostTitle
    private String title;

    @PostSummary
    private String summary;

    @PostImageURL
    private String imageURL;

    private LocalDateTime publishDtm;

    @PostReadingTime
    private Long readingTime;

    @PostContent
    private String content;

    @PostTag
    private Set<TagDTO> tags;

    @PostAuthorID
    private UUID authorID;

    @PostAuthorName
    private String authorName;

    @PostAuthorDisplayName
    private String authorDisplayName;

    @PostAuthorAvatarURL
    private String authorAvatarURL;

    @PostCategoryID
    private Long categoryID;

    @PostCategoryName
    private String categoryName;
}
