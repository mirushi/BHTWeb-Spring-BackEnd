package com.bhtcnpm.website.security.predicate.PostComment;

import com.bhtcnpm.website.constant.domain.Post.PostBusinessState;
import com.bhtcnpm.website.constant.domain.PostComment.PostCommentBusinessState;
import com.querydsl.core.types.dsl.BooleanExpression;

public class PostCommentPredicateGenerator {
    public static BooleanExpression getBooleanExpressionOnBusinessState (PostCommentBusinessState postCommentBusinessState) {
        if (PostCommentBusinessState.PUBLIC.equals(postCommentBusinessState)) {
            return PostCommentPredicate.postCommentPublicBusinessState();
        }
        if (PostCommentBusinessState.DELETE.equals(postCommentBusinessState)) {
            return PostCommentPredicate.deleted();
        }

        throw new IllegalArgumentException("PostCommentBusinessState is not supported in PostCommentPredicateGenerator.");
    }
}
