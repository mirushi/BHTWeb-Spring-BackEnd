package com.bhtcnpm.website.model.validator.entity.PostCommentReport;

import com.bhtcnpm.website.model.entity.PostCommentEntities.PostCommentReport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidPCREntityValidator implements ConstraintValidator<ValidPCREntity, PostCommentReport> {
    @Override
    public void initialize(ValidPCREntity constraintAnnotation) {
    }

    @Override
    public boolean isValid(PostCommentReport value, ConstraintValidatorContext context) {
        //If one of those field is present, it means that the report got handled.
        //Therefore, we need to make sure that all the values got value.
        if (value.getResolvedTime() != null || value.getResolvedBy() != null || value.getResolvedNote() != null) {
            if (value.getResolvedTime() != null && value.getResolvedBy() != null && value.getResolvedNote() != null) {
                return true;
            }
        } else {
            return true;
        }

        return false;
    }
}
