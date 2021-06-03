package com.bhtcnpm.website.model.validator.PostCommentReport;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPCREntityValidator.class)
public @interface ValidPCREntity {
    String message() default "${fieldName} is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
