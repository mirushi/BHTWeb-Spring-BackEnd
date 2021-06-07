package com.bhtcnpm.website.model.validator.dto.Post;

import com.bhtcnpm.website.constant.business.GenericBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotNull
@Size(max = GenericBusinessConstant.URL_MAX_LENGTH)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostAuthorAvatarURL {
    String message() default "Post author avatar URL is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
