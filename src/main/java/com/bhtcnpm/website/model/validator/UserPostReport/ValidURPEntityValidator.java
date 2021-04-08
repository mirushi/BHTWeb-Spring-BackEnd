package com.bhtcnpm.website.model.validator.UserPostReport;

import com.bhtcnpm.website.model.entity.PostEntities.PostReport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidURPEntityValidator implements ConstraintValidator <ValidUPREntity, PostReport> {
    @Override
    public void initialize(ValidUPREntity constraintAnnotation) {

    }

    @Override
    public boolean isValid(PostReport value, ConstraintValidatorContext context) {
        //If one of those field is present, it means that the report got handled.
        //Therefore, we need to make sure that all the values got value.
        if (value.getResolvedTime() != null || value.getResolvedBy() != null || value.getActionTaken() != null) {
            if (value.getResolvedTime() != null && value.getResolvedBy() != null && value.getActionTaken() != null) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}
