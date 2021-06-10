package com.bhtcnpm.website.model.validator.dto.PostReport;

import com.bhtcnpm.website.constant.business.PostReport.PostReportBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size(
        min = PostReportBusinessConstant.REPORT_REASON_MIN,
        max = PostReportBusinessConstant.REPORT_REASON_MAX
)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReportReasonSize {
    String message() default "Post report reason size is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
