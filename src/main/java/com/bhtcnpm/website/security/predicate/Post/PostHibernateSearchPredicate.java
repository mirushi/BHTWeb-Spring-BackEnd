package com.bhtcnpm.website.security.predicate.Post;

import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.mapper.orm.scope.SearchScope;

import java.time.LocalDateTime;

public class PostHibernateSearchPredicate {
    public static SearchPredicate approved(SearchScope<Post> scope) {
        return scope.predicate().match()
                .field("postState")
                .matching(PostStateType.APPROVED).toPredicate();
    }

    public static SearchPredicate notApproved(SearchScope<Post> scope) {
        return scope.predicate().bool().mustNot(approved(scope)).toPredicate();
    }

    public static SearchPredicate deleted (SearchScope<Post> scope) {
        return scope.predicate().exists()
                .field("deletedDate").toPredicate();
    }

    public static SearchPredicate notDeleted (SearchScope<Post> scope) {
        return scope.predicate().bool().mustNot(deleted(scope)).toPredicate();
    }

    public static SearchPredicate postPublishDtmReached (SearchScope<Post> scope) {
        return scope.predicate().range().field("publishDtm").lessThan(LocalDateTime.now()).toPredicate();
    }

    public static SearchPredicate postPublishDtmNotReached (SearchScope<Post> scope) {
        return scope.predicate().range().field("publishDtm").atLeast(LocalDateTime.now()).toPredicate();
    }

    public static SearchPredicate postPublicBusinessState(SearchScope<Post> scope) {
        return scope.predicate().bool()
                .must(approved(scope))
                .must(notDeleted(scope))
                .must(postPublishDtmReached(scope))
                .toPredicate();
    }

    public static SearchPredicate postUnlistedBusinessState(SearchScope<Post> scope) {
        return scope.predicate().bool()
                .must(notDeleted(scope))
                .must(
                        scope.predicate().bool()
                                .should(notApproved(scope))
                                .should(postPublishDtmNotReached(scope))
                )
                .toPredicate();
    }

}
