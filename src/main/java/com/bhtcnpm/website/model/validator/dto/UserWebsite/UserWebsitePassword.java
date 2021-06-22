package com.bhtcnpm.website.model.validator.dto.UserWebsite;

import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank
@Size(min = UWBusinessConstraint.MIN_PASSWORD_LENGTH, max = UWBusinessConstraint.MAX_PASSWORD_LENGTH)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserWebsitePassword {
    String message() default "User website password is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
