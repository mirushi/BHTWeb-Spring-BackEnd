package com.bhtcnpm.website.model.entity.enumeration.ActivityType;

import javax.persistence.AttributeConverter;

public class ActivityTypeConverter implements AttributeConverter<ActivityType, Short> {

    //After persisting, please do not change below code.
    //Only adding is allowed.

    private static final short POST_APPROVED_NUM = 1;
    private static final short POST_CREATED_NUM = 2;

    @Override
    public Short convertToDatabaseColumn(ActivityType attribute) {
        switch (attribute) {
            case POST_APPROVED: {
                return POST_APPROVED_NUM;
            }
            case POST_CREATED: {
                return POST_CREATED_NUM;
            }
        }
        throw new IllegalArgumentException(attribute + " not supported.");
    }

    @Override
    public ActivityType convertToEntityAttribute(Short dbData) {
        switch (dbData) {
            case POST_APPROVED_NUM: {
                return ActivityType.POST_APPROVED;
            }
            case POST_CREATED_NUM: {
                return ActivityType.POST_CREATED;
            }
        }
        throw new IllegalArgumentException(dbData + " not supported.");
    }
}
