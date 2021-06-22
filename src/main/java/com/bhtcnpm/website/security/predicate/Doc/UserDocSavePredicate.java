package com.bhtcnpm.website.security.predicate.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.QUserDocSave;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDocSavePredicate {
    private static QUserDocSave qUserDocSave = QUserDocSave.userDocSave;

    public static BooleanExpression approved() {return qUserDocSave.userDocSaveId.doc.docState.eq(DocStateType.APPROVED);}
    public static BooleanExpression notApproved() {return qUserDocSave.userDocSaveId.doc.docState.ne(DocStateType.APPROVED);}
    public static BooleanExpression deleted() {return qUserDocSave.userDocSaveId.doc.deletedDate.isNotNull();}
    public static BooleanExpression notDeleted() {return qUserDocSave.userDocSaveId.doc.deletedDate.isNull();}

    public static BooleanExpression userOwn (UUID userID) {return qUserDocSave.userDocSaveId.user.id.eq(userID);}
    public static BooleanExpression docPublishDtmReached() {return qUserDocSave.userDocSaveId.doc.publishDtm.loe(LocalDateTime.now());}
    public static BooleanExpression docPublishDtmNotReached() {return qUserDocSave.userDocSaveId.doc.publishDtm.gt(LocalDateTime.now());}

    public static BooleanExpression docPublicBusinessState() {
        return approved().and(notDeleted()).and(docPublishDtmReached());
    }

}
