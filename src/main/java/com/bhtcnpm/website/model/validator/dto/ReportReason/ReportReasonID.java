package com.bhtcnpm.website.model.validator.dto.ReportReason;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Min;
import java.lang.annotation.*;

@Min(0)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportReasonID {
    String message() default "Report reason id is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
