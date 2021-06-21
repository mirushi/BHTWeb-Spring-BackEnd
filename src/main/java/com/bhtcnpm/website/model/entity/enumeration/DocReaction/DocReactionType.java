package com.bhtcnpm.website.model.entity.enumeration.DocReaction;

import com.bhtcnpm.website.constant.domain.Doc.DocReactionTypeConstant;

public enum DocReactionType {
    LIKE(DocReactionTypeConstant.LIKE_ORDINAL),
    DISLIKE(DocReactionTypeConstant.DISLIKE_ORDINAL),
    NONE(DocReactionTypeConstant.NONE_ORDINAL);

    private final short numVal;

    DocReactionType(short enumOrdinal){
        this.numVal = enumOrdinal;
    }

    public short getNumVal() {
        return numVal;
    }
}
