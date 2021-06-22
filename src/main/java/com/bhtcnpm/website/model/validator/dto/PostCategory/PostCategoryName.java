package com.bhtcnpm.website.model.validator.dto.PostCategory;

import com.bhtcnpm.website.constant.business.PostCategory.PostCategoryBusinessConstant;
import com.bhtcnpm.website.constant.message.PostCategory.PostCategoryValidationErrorMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank
@Size (min = PostCategoryBusinessConstant.POST_CATEGORY_NAME_LENGTH_MIN,
        max = PostCategoryBusinessConstant.POST_CATEGORY_NAME_LENGTH_MAX, message = PostCategoryValidationErrorMessage.NAME_SIZE_IS_NOT_VALID)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostCategoryName {
    String message() default "Post category name is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
