package com.bhtcnpm.website.security.predicate.Doc;

import com.bhtcnpm.website.security.util.SecurityUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public class UserDocSavePredicateGenerator {
    public static BooleanExpression getBooleanExpressionUserOwn (Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);
        return UserDocSavePredicate.userOwn(userID);
    }
}
