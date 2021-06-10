package com.bhtcnpm.website.model.validator.dto.PostReport;

import com.bhtcnpm.website.constant.business.PostReport.PostReportBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotBlank
@Size(
        min = PostReportBusinessConstant.CONTENT_FEEDBACK_MIN,
        max = PostReportBusinessConstant.CONTENT_FEEDBACK_MAX
)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReportFeedback {
    String message() default "Post report feedback is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
