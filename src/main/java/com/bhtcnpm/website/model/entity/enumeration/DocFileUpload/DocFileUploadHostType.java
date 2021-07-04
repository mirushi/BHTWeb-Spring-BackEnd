package com.bhtcnpm.website.model.entity.enumeration.DocFileUpload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocFileUploadHostType {
    G_DRIVE((short)0);

    private final short numVal;

    DocFileUploadHostType(short numVal) {this.numVal = numVal;}

    public short getNumVal() {return numVal;}

    @JsonCreator
    public static DocFileUploadHostType forValue (String value) {
        if (G_DRIVE.name().equals(value)) {
            return G_DRIVE;
        }

        throw new IllegalArgumentException(String.format("Value %s does not match any DocFileUploadServerType.", value));
    }

    @JsonValue
    public String toValue () {return this.name();}
}
