package com.bhtcnpm.website.model.validator.dto.PostComment;

import com.bhtcnpm.website.constant.business.PostComment.PostCommentBusinessConstant;
import com.bhtcnpm.website.constant.message.PostComment.PostCommentValidationErrorMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank (message = PostCommentValidationErrorMessage.CONTENT_IS_BLANK_ERROR)
@Size(
        min = PostCommentBusinessConstant.CONTENT_HTML_MIN,
        max = PostCommentBusinessConstant.CONTENT_HTML_MAX,
        message = PostCommentValidationErrorMessage.CONTENT_LENGTH_IS_INVALID
)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostCommentContent {
    String message() default "Post comment content is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
