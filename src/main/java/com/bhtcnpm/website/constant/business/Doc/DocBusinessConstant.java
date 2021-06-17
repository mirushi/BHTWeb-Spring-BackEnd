package com.bhtcnpm.website.constant.business.Doc;

public class DocBusinessConstant {

    //This constant is for minimum length that a title must have.
    public static final int TITLE_MIN = 5;
    //This constant is for maximum length that a title can have.
    public static final int TITLE_MAX = 128;

    //This constant is for minimum length that a description must have.
    public static final int DESCRIPTION_MIN = 5;
    //This constant is for maximum length that a description can have.
    public static final int DESCRIPTION_MAX = 255;

    public static final int TAG_MAX_PER_DOC = 5;

    //This constant is for maximum docs that will be sent to client when it asks for related docs.
    public static final int RELATED_DOC_MAX = 3;

    //This specify how much score boost apply to certain field.
    public static final float SEARCH_TITLE_BOOST = 2.0f;
    public static final float SEARCH_CONTENT_BOOST = 1.0f;
    public static final float SEARCH_DESCRIPTION_BOOST = 1.5f;

}
