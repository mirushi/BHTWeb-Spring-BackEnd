package com.bhtcnpm.website.model.entity.enumeration.DocReaction;

import com.bhtcnpm.website.constant.domain.Doc.DocReactionTypeConstant;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonCreator
    public static DocReactionType forValue (String value) {
        if (LIKE.name().equals(value)) {
            return LIKE;
        }
        if (DISLIKE.name().equals(value)) {
            return DISLIKE;
        }
        return NONE;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
