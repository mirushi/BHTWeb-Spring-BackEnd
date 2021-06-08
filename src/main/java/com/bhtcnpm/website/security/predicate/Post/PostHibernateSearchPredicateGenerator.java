package com.bhtcnpm.website.security.predicate.Post;

import com.bhtcnpm.website.constant.domain.Post.PostBusinessState;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.springframework.security.core.Authentication;

public class PostHibernateSearchPredicateGenerator {
    public static SearchPredicate getSearchPredicateOnBusinessState (PostBusinessState postBusinessState, SearchScope<Post> scope) {
        if (postBusinessState == null) {
            return null;
        }
        if (PostBusinessState.PUBLIC.equals(postBusinessState)) {
            return PostHibernateSearchPredicate.postPublicBusinessState(scope);
        }
        if (PostBusinessState.UNLISTED.equals(postBusinessState)) {
            return PostHibernateSearchPredicate.postUnlistedBusinessState(scope);
        }
        if (PostBusinessState.DELETED.equals(postBusinessState)) {
            return PostHibernateSearchPredicate.deleted(scope);
        }
        throw new IllegalArgumentException("PostBusinessState is not supported.");
    }

    public static SearchPredicate getSearchPredicateOnAuthentication (Authentication authentication, SearchScope<Post> scope) {
        SearchPredicate searchPredicate = null;

        if (PostAccessEvaluator.hasAccessToPublicPost(authentication)) {
            searchPredicate = getNewOrAddToExistingSearchPredicate(
                    searchPredicate,
                    PostHibernateSearchPredicate.postPublicBusinessState(scope),
                    scope
            );
        }
        if (PostAccessEvaluator.hasAccessToUnlistedPost(authentication)) {
            searchPredicate = getNewOrAddToExistingSearchPredicate(
                    searchPredicate,
                    PostHibernateSearchPredicate.postUnlistedBusinessState(scope),
                    scope
            );
        }
        if (PostAccessEvaluator.hasAccessToDeletedPost(authentication)) {
            searchPredicate = getNewOrAddToExistingSearchPredicate(
                    searchPredicate,
                    PostHibernateSearchPredicate.deleted(scope),
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
                                                                         SearchScope<Post> scope) {
        if (currentSearchPredicate == null) {
            return newSearchPredicate;
        }
        return scope.predicate().bool().should(currentSearchPredicate).should(newSearchPredicate).toPredicate();
    }
}
