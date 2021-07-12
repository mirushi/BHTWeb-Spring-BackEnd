package com.bhtcnpm.website.model.validator.dto.Subject;

import com.bhtcnpm.website.constant.business.Subject.SubjectBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank
@Size(min = SubjectBusinessConstant.SUBJECT_NAME_LENGTH_MIN,
        max = SubjectBusinessConstant.SUBJECT_NAME_LENGTH_MAX)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SubjectName {
    String message() default "DocSubjectName is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
