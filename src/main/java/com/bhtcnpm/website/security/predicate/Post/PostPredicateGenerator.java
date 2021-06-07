package com.bhtcnpm.website.security.predicate.Post;

import com.bhtcnpm.website.constant.security.permission.PostPermissionConstant;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;

@Slf4j
public class PostPredicateGenerator {
    public static BooleanExpression getBooleanExpressionOnAuthentication (Authentication authentication) {
        //Don't allow any post to go through if the permission of user is undefined.
        BooleanExpression result = Expressions.FALSE.isTrue();

        if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_PUBLIC_ALL_READ)
                || SecurityUtils.isAnonymousUser(authentication)) {
            result = PostPredicate.postPublicBusinessState();
        }
        if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_UNLISTED_ALL_READ)) {
            result = result.or(PostPredicate.postUnlistedBusinessState());
        }
        if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_DELETED_ALL_READ)) {
            result = result.or(PostPredicate.deleted());
        }

        return result;
    }
}
