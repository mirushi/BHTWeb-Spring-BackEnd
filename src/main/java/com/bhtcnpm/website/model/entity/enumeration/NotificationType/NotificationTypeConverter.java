package com.bhtcnpm.website.model.entity.enumeration.NotificationType;

import javax.persistence.AttributeConverter;

public class NotificationTypeConverter implements AttributeConverter<NotificationType, Short> {

    //After persisting, please do not change below code.
    //Only adding is allowed.

    private static final short POST_LIKE_NUM = 1;
    private static final short POST_COMMENT_NUM = 2;

    @Override
    public Short convertToDatabaseColumn(NotificationType attribute) {
        switch (attribute) {
            case POST_LIKE: {
                return POST_LIKE_NUM;
            }
            case POST_COMMENT: {
                return POST_COMMENT_NUM;
            }
        }
        throw new IllegalArgumentException(attribute + " not supported.");
    }

    @Override
    public NotificationType convertToEntityAttribute(Short dbData) {
        switch (dbData) {
            case POST_LIKE_NUM: {
                return NotificationType.POST_LIKE;
            }
            case POST_COMMENT_NUM: {
                return NotificationType.POST_COMMENT;
            }
        }
        throw new IllegalArgumentException(dbData + " not supported.");
    }
}
