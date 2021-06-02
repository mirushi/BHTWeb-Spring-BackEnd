package com.bhtcnpm.website.security.evaluator.base;

import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

//Only differences is that we omit the targetType, because it was already determined ahead.
public interface SimplePermissionEvaluator extends AopInfrastructureBean {
    boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission);
    boolean hasPermission(Authentication authentication, Serializable targetId, String permission);
}
