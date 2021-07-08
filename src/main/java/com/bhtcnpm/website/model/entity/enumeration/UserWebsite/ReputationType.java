package com.bhtcnpm.website.model.entity.enumeration.UserWebsite;

import com.bhtcnpm.website.constant.domain.UserWebsite.ReputationTypeConstant;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReputationType {
    POST_LIKED (ReputationTypeConstant.POST_LIKED_ORDINAL),
    POST_REPORTED_REMOVED (ReputationTypeConstant.POST_REPORTED_REMOVED_ORDINAL),
    DOC_LIKED (ReputationTypeConstant.DOC_LIKED_ORDINAL),
    DOC_DISLIKED (ReputationTypeConstant.DOC_DISLIKED_ORDINAL),
    DOC_REPORTED_REMOVED (ReputationTypeConstant.DOC_REPORTED_REMOVED_ORDINAL),
    COMMENT_LIKED (ReputationTypeConstant.COMMENT_LIKED_ORDINAL),
    COMMENT_REPORTED_REMOVED (ReputationTypeConstant.COMMENT_REPORTED_REMOVED_ORDINAL);

    private final short numVal;

    ReputationType (short enumOrdinal) { this.numVal = enumOrdinal; }

    public short getNumVal () { return numVal; }

    @JsonCreator
    public static ReputationType forValue (String value) {
        if (POST_LIKED.name().equals(value)) {
            return POST_LIKED;
        }
        if (POST_REPORTED_REMOVED.name().equals(value)) {
            return POST_REPORTED_REMOVED;
        }
        if (DOC_LIKED.name().equals(value)) {
            return DOC_LIKED;
        }
        if (DOC_DISLIKED.name().equals(value)) {
            return DOC_DISLIKED;
        }
        if (DOC_REPORTED_REMOVED.name().equals(value)) {
            return DOC_REPORTED_REMOVED;
        }
        if (COMMENT_LIKED.name().equals(value)) {
            return COMMENT_LIKED;
        }
        if (COMMENT_REPORTED_REMOVED.name().equals(value)) {
            return COMMENT_REPORTED_REMOVED;
        }
        throw new IllegalArgumentException(String.format("Enum name %s does not exists in ReputationType.", value));
    }

    @JsonValue
    public String toValue () { return this.name(); }
}
