package com.bhtcnpm.website.model.validator.dto.Post;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@NotNull
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostAuthorID {
    String message() default "Post author ID is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
