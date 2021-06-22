package com.bhtcnpm.website.security.predicate.PostComment;

import com.bhtcnpm.website.model.entity.PostCommentEntities.QPostComment;
import com.querydsl.core.types.dsl.BooleanExpression;

//Chỉ nên sử dụng class này trong class PostCommentPredicateGenerator.
public class PostCommentPredicate {
    private static QPostComment qPostComment = QPostComment.postComment;

    public static BooleanExpression notDeleted() {return qPostComment.deletedDate.isNull();}

    public static BooleanExpression deleted() {return qPostComment.deletedDate.isNotNull();}

    public static BooleanExpression postCommentPublicBusinessState () {return notDeleted();}
}
