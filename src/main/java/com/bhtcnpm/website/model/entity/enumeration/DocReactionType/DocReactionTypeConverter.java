package com.bhtcnpm.website.model.entity.enumeration.DocReactionType;

import com.bhtcnpm.website.model.entity.enumeration.CourseContentType.CourseContentType;

import javax.persistence.AttributeConverter;

public class DocReactionTypeConverter implements AttributeConverter<DocReactionType, Short> {

    //After persisting, please do not change below code.
    //Only adding is allowed.

    private static final short LIKE_NUM = 1;
    private static final short DISLIKE_NUM = 2;

    @Override
    public Short convertToDatabaseColumn(DocReactionType attribute) {
        switch (attribute) {
            case LIKE: {
                return LIKE_NUM;
            }
            case DISLIKE: {
                return DISLIKE_NUM;
            }
        }
        throw new IllegalArgumentException(attribute + " not supported.");
    }

    @Override
    public DocReactionType convertToEntityAttribute(Short dbData) {
        switch (dbData) {
            case LIKE_NUM: {
                return DocReactionType.LIKE;
            }
            case DISLIKE_NUM: {
                return DocReactionType.DISLIKE;
            }
        }
        throw new IllegalArgumentException(dbData + " not supported.");
    }
}
