package com.bhtcnpm.website.model.validator.dto.Post;

import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;
import com.bhtcnpm.website.constant.message.Post.PostValidationErrorMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size(max = PostBusinessConstant.POST_STATISTICS_MAX)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostStatisticRequestSize {
    String message() default PostValidationErrorMessage.REQUEST_SIZE_TOO_BIG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
