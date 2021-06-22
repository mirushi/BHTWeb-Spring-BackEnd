package com.bhtcnpm.website.model.validator.dto.PostReport;

import com.bhtcnpm.website.constant.business.PostReport.PostReportBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size(
        min = PostReportBusinessConstant.REPORT_RESOLVE_NOTE_LENGTH_MIN,
        max = PostReportBusinessConstant.REPORT_RESOLVE_NOTE_LENGTH_MAX
)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReportResolvedNote {
    String message() default "Post report resolved note length is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
