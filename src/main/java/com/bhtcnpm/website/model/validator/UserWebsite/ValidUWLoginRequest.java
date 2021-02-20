package com.bhtcnpm.website.model.validator.UserWebsite;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidUWLoginRequestValidator.class})
@Documented
public @interface ValidUWLoginRequest {
    String message () default "Invalid login credentials.";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}
