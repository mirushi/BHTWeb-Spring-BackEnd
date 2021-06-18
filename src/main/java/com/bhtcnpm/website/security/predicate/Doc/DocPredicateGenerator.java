package com.bhtcnpm.website.security.predicate.Doc;

import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.core.Authentication;

import java.util.UUID;

//Chỉ nên sử dụng class này expose ra bên ngoài.
public class DocPredicateGenerator {
    public static BooleanExpression getBooleanExpressionNotDeleted() {return DocPredicate.notDeleted();}
    public static BooleanExpression getBooleanExpressionOnBusinessState(DocBusinessState docBusinessState) {
        if (DocBusinessState.PUBLIC.equals(docBusinessState)) {
            return DocPredicate.docPublicBusinessState();
        }
        if (DocBusinessState.UNLISTED.equals(docBusinessState)) {
            return DocPredicate.docUnlistedBusinessState();
        }
        if (DocBusinessState.DELETED.equals(docBusinessState)) {
            return DocPredicate.deleted();
        }
        throw new IllegalArgumentException("DocBusinessState is not supported in DocPredicateGenerator.");
    }
    public static BooleanExpression getBooleanExpressionUserOwn (Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        return DocPredicate.userOwn(userID);
    }
    public static BooleanExpression getBooleanExpressionOnAuthentication (Authentication authentication) {
        //Don't allow any post to go through if the permission of user is undefined.
        BooleanExpression result = Expressions.FALSE.isTrue();

        if (DocAccessEvaluator.hasAccessToPublicDoc(authentication)) {
            result = result.or(DocPredicate.docPublicBusinessState());
        }
        if (DocAccessEvaluator.hasAccessToUnlistedDoc(authentication)) {
            result = result.or(DocPredicate.docUnlistedBusinessState());
        }
        if (DocAccessEvaluator.hasAccessToDeletedDoc(authentication)) {
            result = result.or(DocPredicate.deleted());
        }

        return result;
    }
}
