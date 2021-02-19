package com.bhtcnpm.website.model.validator.UserWebsite;

import com.bhtcnpm.website.model.dto.UserWebsite.UserWebsiteCreateNewRequestDTO;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ValidUWCreateNewRequestValidator implements ConstraintValidator<ValidUWCreateNewRequest, UserWebsiteCreateNewRequestDTO> {

    @Autowired
    private UserWebsiteRepository userWebsiteRepository;

    private ValidUWCreateNewRequest constraintAnnotation;

    @Override
    public void initialize(ValidUWCreateNewRequest constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(UserWebsiteCreateNewRequestDTO value, ConstraintValidatorContext context) {
        Optional<UserWebsite> user = userWebsiteRepository.findByNameOrDisplayNameOrEmail(value.getName(), value.getDisplayName(), value.getEmail());

        if (user.isPresent()) {
            HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);

            UserWebsite currentUser = user.get();
            String existedField = "";

            if (currentUser.getName().equals(value.getName())) {
                existedField = "Name";
            } else if (currentUser.getDisplayName().equals(value.getDisplayName())) {
                existedField = "Display Name";
            } else if (currentUser.getEmail().equals(value.getEmail())) {
                existedField = "Email";
            }

            if (StringUtils.isNotBlank(existedField)) {
                hibernateContext.disableDefaultConstraintViolation();
                hibernateContext.addExpressionVariable("fieldName", existedField)
                        .buildConstraintViolationWithTemplate(constraintAnnotation.message())
                        .addConstraintViolation();
            }
            return false;
        } else {
            return true;
        }
    }
}
