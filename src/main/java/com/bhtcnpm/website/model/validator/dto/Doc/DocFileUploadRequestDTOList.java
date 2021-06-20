package com.bhtcnpm.website.model.validator.dto.Doc;

import com.bhtcnpm.website.constant.business.Doc.DocBusinessConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size(max = DocBusinessConstant.DOCFILE_MAX_PER_DOC)
@Constraint(validatedBy = {})
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocFileUploadRequestDTOList {
    String message() default "Doc file upload request list is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
