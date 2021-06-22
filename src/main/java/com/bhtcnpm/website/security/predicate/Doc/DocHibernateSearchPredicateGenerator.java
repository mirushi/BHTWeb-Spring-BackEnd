package com.bhtcnpm.website.security.predicate.Doc;

import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.springframework.security.core.Authentication;

public class DocHibernateSearchPredicateGenerator {
    public static SearchPredicate getSearchPredicateOnBusinessState (DocBusinessState docBusinessState, SearchScope<Doc> scope) {
        if (docBusinessState == null){
            return null;
        }
        if (DocBusinessState.PUBLIC.equals(docBusinessState)) {
            return DocHibernateSearchPredicate.docPublicBusinessState(scope);
        }
        if (DocBusinessState.UNLISTED.equals(docBusinessState)) {
            return DocHibernateSearchPredicate.docUnlistedBusinessState(scope);
        }
        if (DocBusinessState.DELETED.equals(docBusinessState)) {
            return DocHibernateSearchPredicate.deleted(scope);
        }
        throw new IllegalArgumentException("DocBusinessState is not supported.");
    }

    public static SearchPredicate getSearchPredicateOnAuthentication (Authentication authentication, SearchScope<Doc> scope) {
        SearchPredicate searchPredicate = null;

        if (DocAccessEvaluator.hasAccessToPublicDoc(authentication)) {
            searchPredicate = getNewOrAddToExistingSearchPredicate(
                    searchPredicate,
                    DocHibernateSearchPredicate.docPublicBusinessState(scope),
                    scope
            );
        }
        if (DocAccessEvaluator.hasAccessToUnlistedDoc(authentication)) {
            searchPredicate = getNewOrAddToExistingSearchPredicate(
                    searchPredicate,
                    DocHibernateSearchPredicate.docUnlistedBusinessState(scope),
                    scope
            );
        }
        if (DocAccessEvaluator.hasAccessToDeletedDoc(authentication)) {
            searchPredicate = getNewOrAddToExistingSearchPredicate(
                    searchPredicate,
                    DocHibernateSearchPredicate.deleted(scope),
                    scope
            );
        }
        if (searchPredicate == null) {
            throw new IllegalArgumentException("Search predicate cannot be determined. User permission unknown.");
        }

        return searchPredicate;
    }

    private static SearchPredicate getNewOrAddToExistingSearchPredicate (SearchPredicate currentSearchPredicate,
                                                                         SearchPredicate newSearchPredicate,
                                                                         SearchScope<Doc> scope) {
        if (currentSearchPredicate == null) {
            return newSearchPredicate;
        }

        return scope.predicate().bool().should(currentSearchPredicate).should(newSearchPredicate).toPredicate();
    }

}
