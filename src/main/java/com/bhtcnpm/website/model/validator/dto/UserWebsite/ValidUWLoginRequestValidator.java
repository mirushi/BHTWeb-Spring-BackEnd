package com.bhtcnpm.website.model.validator.dto.UserWebsite;

import com.bhtcnpm.website.model.dto.UserWebsite.UserWebsiteLoginRequestDTO;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidUWLoginRequestValidator implements ConstraintValidator<ValidUWLoginRequest, UserWebsiteLoginRequestDTO> {

    @Override
    public void initialize(ValidUWLoginRequest constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserWebsiteLoginRequestDTO value, ConstraintValidatorContext context) {
        //Login request is valid if and only if user provided either username or email.
        if (StringUtils.isAllBlank(value.getEmail(), value.getUsername())) {
            return false;
        } else if (StringUtils.isNotBlank(value.getEmail()) && StringUtils.isNotBlank(value.getUsername())) {
            return false;
        }

        return true;
    }
}
