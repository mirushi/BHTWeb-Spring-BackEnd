package com.bhtcnpm.website.security.predicate.Post;

import com.bhtcnpm.website.constant.domain.Post.PostBusinessState;
import com.bhtcnpm.website.constant.security.permission.PostPermissionConstant;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.util.UUID;

//Chỉ nên sử dụng class này expose ra bên ngoài.
@Slf4j
public class PostPredicateGenerator {

    public static BooleanExpression getBooleanExpressionNotDeleted() {
        return PostPredicate.notDeleted();
    }

    public static BooleanExpression getBooleanExpressionOnBusinessState(PostBusinessState postBusinessState) {
        if (PostBusinessState.PUBLIC.equals(postBusinessState)) {
            return PostPredicate.postPublicBusinessState();
        }
        if (PostBusinessState.UNLISTED.equals(postBusinessState)) {
            return PostPredicate.postUnlistedBusinessState();
        }
        if (PostBusinessState.DELETED.equals(postBusinessState)) {
            return PostPredicate.deleted();
        }
        throw new IllegalArgumentException("PostBusinessState is not supported in PostPredicateGenerator.");
    }

    public static BooleanExpression getBooleanExpressionUserOwn (Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        if (userID == null) {
            throw new IllegalArgumentException("Cannot extract userID from authentication.");
        }

        return PostPredicate.userOwn(userID);
    }

    public static BooleanExpression getBooleanExpressionOnAuthentication (Authentication authentication) {
        //Don't allow any post to go through if the permission of user is undefined.
        BooleanExpression result = Expressions.FALSE.isTrue();

        if (PostAccessEvaluator.hasAccessToPublicPost(authentication)) {
            result = result.or(PostPredicate.postPublicBusinessState());
        }
        if (PostAccessEvaluator.hasAccessToUnlistedPost(authentication)) {
            result = result.or(PostPredicate.postUnlistedBusinessState());
        }
        if (PostAccessEvaluator.hasAccessToDeletedPost(authentication)) {
            result = result.or(PostPredicate.deleted());
        }

        return result;
    }
}
