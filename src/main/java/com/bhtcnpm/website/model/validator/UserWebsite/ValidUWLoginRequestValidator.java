package com.bhtcnpm.website.model.validator.UserWebsite;

import com.bhtcnpm.website.model.dto.UserWebsite.UserWebsiteLoginRequestDTO;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

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
