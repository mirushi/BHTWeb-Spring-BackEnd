package com.bhtcnpm.website.security.predicate.Doc;

import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public class UserDocSavePredicateGenerator {
    public static BooleanExpression getBooleanExpressionUserOwn (Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);
        return UserDocSavePredicate.userOwn(userID);
    }
    public static BooleanExpression getBooleanExpressionOnDocBusinessState (DocBusinessState docBusinessState) {
        if (DocBusinessState.PUBLIC.equals(docBusinessState)) {
            return UserDocSavePredicate.docPublicBusinessState();
        }
        throw new UnsupportedOperationException("This business state is not supported by User doc save generator.");
    }

    public static BooleanExpression getBooleanExpressionOnAuthentication (Authentication authentication) {
        //Don't allow any post to go through if the permission of user is undefined.
        BooleanExpression result = Expressions.FALSE.isTrue();

        if (DocAccessEvaluator.hasAccessToPublicDoc(authentication)) {
            result = result.or(UserDocSavePredicate.docPublicBusinessState());
        }

        return result;
    }
}
