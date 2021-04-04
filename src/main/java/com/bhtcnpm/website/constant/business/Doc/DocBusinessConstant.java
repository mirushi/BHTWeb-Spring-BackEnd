package com.bhtcnpm.website.constant.business.Doc;

public class DocBusinessConstant {
    //This constant is for maximum docs that will be sent to client when it asks for related docs.
    public static final int RELATED_DOC_MAX = 3;

    //This specify how much score boost apply to certain field.
    public static final float SEARCH_TITLE_BOOST = 2.0f;
    public static final float SEARCH_CONTENT_BOOST = 1.0f;
    public static final float SEARCH_DESCRIPTION_BOOST = 1.5f;

}
