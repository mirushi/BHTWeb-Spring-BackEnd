package com.bhtcnpm.website.model.validator.dto.Doc;

import com.bhtcnpm.website.constant.business.Doc.DocBusinessConstant;
import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank
@Size(min = DocBusinessConstant.DESCRIPTION_MIN, max = DocBusinessConstant.DESCRIPTION_MAX)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocDescription {
    String message() default "Doc description is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
