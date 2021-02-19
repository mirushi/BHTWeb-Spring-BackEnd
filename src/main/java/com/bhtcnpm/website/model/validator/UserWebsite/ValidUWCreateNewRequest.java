package com.bhtcnpm.website.model.validator.UserWebsite;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidUWCreateNewRequestValidator.class})
@Documented
public @interface ValidUWCreateNewRequest {
    String message () default "${fieldName} already existed.";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}
