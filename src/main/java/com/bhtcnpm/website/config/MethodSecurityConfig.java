package com.bhtcnpm.website.config;

import com.bhtcnpm.website.security.evaluator.EntityPermissionEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        //The prePostEnabled property enables Spring Security pre/post annotations
        prePostEnabled = true,
        //The securedEnabled property determines if the @Secured annotation should be enabled
        securedEnabled = true,
        //The jsr250Enabled property allows us to use the @RoleAllowed annotation
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final EntityPermissionEvaluator entityPermissionEvaluator;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(entityPermissionEvaluator);
        return expressionHandler;
    }
}
