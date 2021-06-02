package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.constant.business.GenericBusinessConstant;
import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;
import com.bhtcnpm.website.constant.business.Post.PostCategoryBusinessConstant;
import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstraint;
import com.bhtcnpm.website.constant.message.Post.PostValidationErrorMessage;
import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import lombok.Data;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class PostDetailsDTO {
    @Min(0)
    private Long id;

    @NotBlank
    @Size(min = PostBusinessConstant.TITLE_MIN, max = PostBusinessConstant.TITLE_MAX)
    private String title;

    @NotBlank
    @Size(min = PostBusinessConstant.SUMMARY_MIN, max = PostBusinessConstant.SUMMARY_MAX)
    private String summary;

    @Pattern(regexp = "(?:([^:/?#]+):)?(?://([^/?#]*))?([^?#]*\\.(?:jpe?g|gif|png))(?:\\?([^#]*))?(?:#(.*))?")
    @Size(max = GenericBusinessConstant.URL_MAX_LENGTH)
    private String imageURL;

    @NotNull
    @Future
    private LocalDateTime publishDtm;

    @Min(0)
    private Long readingTime;

    @NotBlank(message = PostValidationErrorMessage.CONTENT_IS_BLANK_ERROR)
    @Size(
            min = PostBusinessConstant.CONTENT_HTML_MIN,
            max = PostBusinessConstant.CONTENT_HTML_MAX,
            message = PostValidationErrorMessage.CONTENT_LENGTH_IS_INVALID
    )
    private String content;

    @Size(max = PostBusinessConstant.TAG_MAX_PER_POST, message = PostValidationErrorMessage.TOO_MANY_TAGS)
    private Set<TagDTO> tags;

    @NotNull
    private UUID authorID;

    @NotNull
    @Size(min = UWBusinessConstraint.MIN_USER_NAME_LENGTH, max = UWBusinessConstraint.MAX_USER_NAME_LENGTH)
    private String authorName;

    @NotNull
    @Size(min = UWBusinessConstraint.MIN_DISPLAY_NAME_LENGTH, max = UWBusinessConstraint.MAX_DISPLAY_NAME_LENGTH)
    private String authorDisplayName;

    @NotNull
    @Size(max = GenericBusinessConstant.URL_MAX_LENGTH)
    private String authorAvatarURL;

    @NotNull
    @Min(0)
    private Long categoryID;

    @NotNull
    @Size(max = PostCategoryBusinessConstant.CATEGORY_NAME_MAX)
    private String categoryName;
}
