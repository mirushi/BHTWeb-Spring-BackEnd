package com.bhtcnpm.website.security.predicate.Post;

import com.bhtcnpm.website.model.entity.PostEntities.QPost;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDateTime;

public class PostPredicate {
    private static QPost qPost = QPost.post;

    public static BooleanExpression approved() {
        return qPost.postState.eq(PostStateType.APPROVED);
    }

    public static BooleanExpression notApproved () {
        return qPost.postState.ne(PostStateType.APPROVED);
    }

    public static BooleanExpression deleted () {
        return qPost.deletedDate.isNotNull();
    }

    public static BooleanExpression notDeleted () {
        return qPost.deletedDate.isNull();
    }

    public static BooleanExpression postPublishDtmReached() {
        return qPost.publishDtm.loe(LocalDateTime.now());
    }

    public static BooleanExpression postPublishDtmNotReached() {
        return qPost.publishDtm.gt(LocalDateTime.now());
    }

    public static BooleanExpression postPublicBusinessState() {
        return approved().and(notDeleted()).and(postPublishDtmReached());
    }

    public static BooleanExpression postUnlistedBusinessState() {
        return notDeleted().and(notApproved().or(postPublishDtmNotReached()));
    }
}
