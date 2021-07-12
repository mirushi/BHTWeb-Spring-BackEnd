package com.bhtcnpm.website.model.validator.dto.DocComment;

import com.bhtcnpm.website.constant.business.DocComment.DocCommentBusinessConstant;
import com.bhtcnpm.website.constant.message.DocComment.DocCommentValidationErrorMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank(message = DocCommentValidationErrorMessage.CONTENT_IS_BLANK_ERROR)
@Size(
        min = DocCommentBusinessConstant.CONTENT_HTML_MIN,
        max = DocCommentBusinessConstant.CONTENT_HTML_MAX,
        message = DocCommentValidationErrorMessage.CONTENT_LENGTH_IS_INVALID
)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocCommentContent {
    String message() default "Post comment content is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
