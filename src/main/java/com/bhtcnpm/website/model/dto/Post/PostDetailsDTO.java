package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.constant.business.GenericBusinessConstant;
import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;
import com.bhtcnpm.website.constant.business.Post.PostCategoryBusinessConstant;
import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstraint;
import com.bhtcnpm.website.constant.message.Post.PostValidationErrorMessage;
import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.validator.dto.Post.*;
import lombok.Data;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.*;
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
