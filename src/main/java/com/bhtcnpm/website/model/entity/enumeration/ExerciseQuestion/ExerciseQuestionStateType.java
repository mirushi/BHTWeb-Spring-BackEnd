package com.bhtcnpm.website.model.entity.enumeration.ExerciseQuestion;

import com.bhtcnpm.website.constant.domain.Exercise.ExerciseStateTypeConstant;
import com.bhtcnpm.website.constant.domain.ExerciseQuestion.ExerciseQuestionStateTypeConstant;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExerciseQuestionStateType {
    APPROVED (ExerciseQuestionStateTypeConstant.APPROVED_ORDINAL),
    PENDING_APPROVAL (ExerciseQuestionStateTypeConstant.PENDING_APPROVAL_ORDINAL),
    PENDING_FIX (ExerciseStateTypeConstant.PENDING_FIX_ORDINAL),
    REJECTED (ExerciseStateTypeConstant.REJECTED_ORDINAL);

    private final short numVal;

    ExerciseQuestionStateType (short enumOrdinal) {this.numVal = enumOrdinal;}

    public short getNumVal() {return numVal;}

    public static ExerciseQuestionStateType forValue (String value) {
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

        throw new IllegalArgumentException(String.format("Value %s does not match any ExerciseQuestionStateType enum.", value));
    }

    @JsonValue
    public String toValue () { return this.name(); }
}
