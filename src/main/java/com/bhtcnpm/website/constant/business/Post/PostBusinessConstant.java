package com.bhtcnpm.website.constant.business.Post;

public class PostBusinessConstant {
    //This constant is for maximum posts that will be sent to client when it asks for related posts.
    public static final int RELATED_POST_MAX = 3;

    //This specify how much score boost apply to certain field.
    public static final float SEARCH_TITLE_BOOST = 2.0f;
    public static final float SEARCH_CONTENT_BOOST = 1.0f;
    public static final float SEARCH_SUMMARY_BOOST = 1.5f;
}
