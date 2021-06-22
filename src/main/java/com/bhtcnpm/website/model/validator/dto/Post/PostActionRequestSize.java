package com.bhtcnpm.website.model.validator.dto.Post;

import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size(max = PostBusinessConstant.POST_ACTION_MAX)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostActionRequestSize {
    String message() default "Post action request size is too big.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
