package com.bhtcnpm.website.model.validator.dto.Doc;

import com.bhtcnpm.website.constant.business.Doc.DocBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size(max = DocBusinessConstant.DOC_ACTION_MAX)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocActionRequestSize {
    String message() default "Doc action request size is too big.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
