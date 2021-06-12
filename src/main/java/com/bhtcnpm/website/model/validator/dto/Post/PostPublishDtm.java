package com.bhtcnpm.website.model.validator.dto.Post;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostPublishDtm {
    String message() default "Post publish datetime is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
