package com.bhtcnpm.website.model.validator.dto.DocCategory;

import com.bhtcnpm.website.constant.business.DocCategory.DocCategoryBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank
@Size(min = DocCategoryBusinessConstant.DOC_CATEGORY_NAME_LENGTH_MIN,
        max = DocCategoryBusinessConstant.DOC_CATEGORY_NAME_LENGTH_MAX)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocCategoryName {
    String message() default "DocCategoryName is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
