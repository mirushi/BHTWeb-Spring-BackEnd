package com.bhtcnpm.website.model.validator.UserPostReport;

import com.bhtcnpm.website.model.validator.UserWebsite.ValidUWCreateNewRequestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidURPEntityValidator.class})
@Documented
public @interface ValidUPREntity {
    String message() default "${fieldName} is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
