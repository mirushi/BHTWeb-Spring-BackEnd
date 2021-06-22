package com.bhtcnpm.website.model.validator.dto.Post;

import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank
@Size(min = PostBusinessConstant.TITLE_MIN, max = PostBusinessConstant.TITLE_MAX)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostTitle {
    String message() default "Post title is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
