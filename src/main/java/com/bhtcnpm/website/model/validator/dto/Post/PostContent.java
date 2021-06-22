package com.bhtcnpm.website.model.validator.dto.Post;

import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;
import com.bhtcnpm.website.constant.message.Post.PostValidationErrorMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank(message = PostValidationErrorMessage.CONTENT_IS_BLANK_ERROR)
@Size(
        min = PostBusinessConstant.CONTENT_HTML_MIN,
        max = PostBusinessConstant.CONTENT_HTML_MAX,
        message = PostValidationErrorMessage.CONTENT_LENGTH_IS_INVALID
)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostContent {
    String message() default "Post content is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
