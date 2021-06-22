package com.bhtcnpm.website.model.validator.dto.UserWebsite;

import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size(max = UWBusinessConstraint.MAX_AVATAR_URL_LENGTH)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserWebsiteAvatarURL {
    String message() default "User website avatar URL is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
