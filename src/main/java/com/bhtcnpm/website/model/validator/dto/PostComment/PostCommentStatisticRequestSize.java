package com.bhtcnpm.website.model.validator.dto.PostComment;

import com.bhtcnpm.website.constant.business.PostComment.PostCommentBusinessConstant;
import com.bhtcnpm.website.constant.message.PostComment.PostCommentValidationErrorMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size(max = PostCommentBusinessConstant.POST_COMMENT_STATISTIC_MAX, message = PostCommentValidationErrorMessage.REQUEST_SIZE_TOO_BIG)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostCommentStatisticRequestSize {
    String message() default PostCommentValidationErrorMessage.REQUEST_SIZE_TOO_BIG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
