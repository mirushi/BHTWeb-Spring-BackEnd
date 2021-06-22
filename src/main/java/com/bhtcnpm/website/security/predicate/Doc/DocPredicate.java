package com.bhtcnpm.website.security.predicate.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.QDoc;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDateTime;
import java.util.UUID;

//Chỉ nên sử dụng class này trong class DocPredicateGenerator.
public class DocPredicate {
    private static QDoc qDoc = QDoc.doc;

    public static BooleanExpression approved () {return qDoc.docState.eq(DocStateType.APPROVED);}
    public static BooleanExpression notApproved() {return qDoc.docState.ne(DocStateType.APPROVED);}
    public static BooleanExpression deleted() {return qDoc.deletedDate.isNotNull(); }
    public static BooleanExpression notDeleted() {return qDoc.deletedDate.isNull();}
    public static BooleanExpression userOwn (UUID userID) {return qDoc.author.id.eq(userID);}
    public static BooleanExpression docPublishDtmReached() {return qDoc.publishDtm.loe(LocalDateTime.now());}
    public static BooleanExpression docPublishDtmNotReached() {return qDoc.publishDtm.gt(LocalDateTime.now());}
    public static BooleanExpression docPublicBusinessState() {
        return approved().and(notDeleted()).and(docPublishDtmReached());
    }
    public static BooleanExpression docUnlistedBusinessState() {
        return notDeleted().and(notApproved().or(docPublishDtmNotReached()));
    }
    public static BooleanExpression docStateType (DocStateType docStateType) {return qDoc.docState.eq(docStateType);}
}
