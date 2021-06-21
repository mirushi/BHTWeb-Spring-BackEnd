package com.bhtcnpm.website.model.entity.enumeration.DocState;

import com.bhtcnpm.website.constant.domain.Doc.DocStateTypeConstant;

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

}
