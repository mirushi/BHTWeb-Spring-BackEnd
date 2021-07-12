package com.bhtcnpm.website.security.predicate.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.mapper.orm.scope.SearchScope;

import java.time.LocalDateTime;

public class DocHibernateSearchPredicate {
    public static SearchPredicate approved (SearchScope<Doc> scope) {
        return scope.predicate().match()
                .field("docState")
                .matching(DocStateType.APPROVED).toPredicate();
    }

    public static SearchPredicate notApproved (SearchScope<Doc> scope) {
        return scope.predicate().bool().mustNot(approved(scope)).toPredicate();
    }

    public static SearchPredicate deleted (SearchScope<Doc> scope) {
        return scope.predicate().exists()
                .field("deletedDtm").toPredicate();
    }

    public static SearchPredicate notDeleted (SearchScope<Doc> scope) {
        return scope.predicate().bool().mustNot(deleted(scope)).toPredicate();
    }

    public static SearchPredicate docPublishDtmReached (SearchScope<Doc> scope) {
        return scope.predicate().range().field("publishDtm").lessThan(LocalDateTime.now()).toPredicate();
    }

    public static SearchPredicate docPublishDtmNotReached (SearchScope<Doc> scope) {
        return scope.predicate().range().field("publishDtm").atLeast(LocalDateTime.now()).toPredicate();
    }

    public static SearchPredicate docPublicBusinessState (SearchScope<Doc> scope) {
        return scope.predicate().bool()
                .must(approved(scope))
                .must(notDeleted(scope))
                .must(docPublishDtmReached(scope))
                .toPredicate();
    }

    public static SearchPredicate docUnlistedBusinessState (SearchScope<Doc> scope) {
        return scope.predicate().bool()
                .must(notDeleted(scope))
                .must(
                        scope.predicate().bool()
                        .should(notApproved(scope))
                        .should(docPublishDtmNotReached(scope))
                )
                .toPredicate();
    }

}
