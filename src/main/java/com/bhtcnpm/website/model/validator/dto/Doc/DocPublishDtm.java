package com.bhtcnpm.website.model.validator.dto.Doc;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocPublishDtm {
    String message() default "Doc publish datetime is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
