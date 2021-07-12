package com.bhtcnpm.website.model.entity.enumeration.DocState;

import com.bhtcnpm.website.constant.domain.Doc.DocStateTypeConstant;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocStateType {
    APPROVED (DocStateTypeConstant.APPROVED_ORDINAL),
    PENDING_APPROVAL (DocStateTypeConstant.PENDING_APPROVAL_ORDINAL),
    PENDING_FIX (DocStateTypeConstant.PENDING_FIX_ORDINAL),
    REJECTED (DocStateTypeConstant.REJECTED_ORDINAL);

    private final short numVal;

    DocStateType (short enumOrdinal) {
        this.numVal = enumOrdinal;
    }

    public short getNumVal () {
        return numVal;
    }

    @JsonCreator
    public static DocStateType forValue (String value) {
        if (APPROVED.name().equals(value)) {
            return APPROVED;
        }
        if (PENDING_APPROVAL.name().equals(value)) {
            return PENDING_APPROVAL;
        }
        if (PENDING_FIX.name().equals(value)) {
            return PENDING_FIX;
        }
        if (REJECTED.name().equals(value)) {
            return REJECTED;
        }

        throw new IllegalArgumentException(String.format("Value %s does not match any DocStateType enum.", value));
    }

    @JsonValue
    public String toValue () {return this.name();}
}
