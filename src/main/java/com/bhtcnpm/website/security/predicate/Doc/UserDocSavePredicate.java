package com.bhtcnpm.website.security.predicate.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.QUserDocSave;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.UUID;

public class UserDocSavePredicate {
    private static QUserDocSave qUserDocSave = QUserDocSave.userDocSave;

    public static BooleanExpression userOwn (UUID userID) {return qUserDocSave.userDocSaveId.user.id.eq(userID);}

}
