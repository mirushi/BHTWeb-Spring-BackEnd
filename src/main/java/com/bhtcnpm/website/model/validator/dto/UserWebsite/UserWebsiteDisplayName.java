package com.bhtcnpm.website.model.validator.dto.UserWebsite;

import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank
@Size(min = UWBusinessConstraint.MIN_DISPLAY_NAME_LENGTH, max = UWBusinessConstraint.MAX_DISPLAY_NAME_LENGTH)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserWebsiteDisplayName {
    String message() default "User website display name is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
