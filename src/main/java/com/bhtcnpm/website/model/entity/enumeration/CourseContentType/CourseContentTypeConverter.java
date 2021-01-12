package com.bhtcnpm.website.model.entity.enumeration.CourseContentType;

import com.bhtcnpm.website.model.entity.enumeration.ActivityType.ActivityType;

import javax.persistence.AttributeConverter;

public class CourseContentTypeConverter implements AttributeConverter<CourseContentType, Short> {

    //After persisting, please do not change below code.
    //Only adding is allowed.

    private static final short ARTICLE_NUM = 1;
    private static final short EXERCISE_MCQ_NUM = 2;
    private static final short EXERCISE_FITB_NUM = 3;

    @Override
    public Short convertToDatabaseColumn(CourseContentType attribute) {
        switch (attribute) {
            case ARTICLE: {
                return ARTICLE_NUM;
            }
            case EXERCISE_MCQ: {
                return EXERCISE_MCQ_NUM;
            }
            case EXERCISE_FITB: {
                return EXERCISE_FITB_NUM;
            }
        }
        throw new IllegalArgumentException(attribute + " not supported.");
    }

    @Override
    public CourseContentType convertToEntityAttribute(Short dbData) {
        switch (dbData) {
            case ARTICLE_NUM: {
                return CourseContentType.ARTICLE;
            }
            case EXERCISE_MCQ_NUM: {
                return CourseContentType.EXERCISE_MCQ;
            }
            case EXERCISE_FITB_NUM: {
                return CourseContentType.EXERCISE_FITB;
            }
        }
        throw new IllegalArgumentException(dbData + " not supported.");
    }
}
