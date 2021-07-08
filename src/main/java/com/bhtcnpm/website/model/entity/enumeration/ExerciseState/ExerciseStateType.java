package com.bhtcnpm.website.model.entity.enumeration.ExerciseState;

import com.bhtcnpm.website.constant.domain.Exercise.ExerciseStateTypeConstant;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExerciseStateType {
    APPROVED (ExerciseStateTypeConstant.APPROVED_ORDINAL),
    PENDING_APPROVAL (ExerciseStateTypeConstant.PENDING_APPROVAL_ORDINAL),
    PENDING_FIX (ExerciseStateTypeConstant.PENDING_FIX_ORDINAL),
    REJECTED (ExerciseStateTypeConstant.REJECTED_ORDINAL);

    private final short numVal;

    ExerciseStateType (short enumOrdinal) {
        this.numVal = enumOrdinal;
    }

    public short getNumVal () { return numVal; }

    @JsonCreator
    public static ExerciseStateType forValue (String value) {
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

        throw new IllegalArgumentException(String.format("Value %s does not match any ExerciseStateType enum.", value));
    }

    @JsonValue
    public String toValue () { return this.name(); }
}
