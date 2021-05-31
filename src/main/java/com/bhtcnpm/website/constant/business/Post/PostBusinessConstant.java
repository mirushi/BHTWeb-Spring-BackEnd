package com.bhtcnpm.website.constant.business.Post;

public class PostBusinessConstant {
    //This constant is for maximum posts that will be sent to client when it asks for related posts.
    public static final int RELATED_POST_MAX = 3;

    //Maximum number of postID that single request of get post statistics can handle.
    public static final int POST_STATISTICS_MAX = 20;

    //This constant is for minimum length that a title must have.
    public static final int TITLE_MIN = 10;
    //This constant is for maximum length that a title can have.
    public static final int TITLE_MAX = 128;

    //This constant is for minimum length that a summary must have.
    public static final int SUMMARY_MIN = 10;
    //This constant is for maximum length that a summary can have.
    public static final int SUMMARY_MAX = 255;

    public static final int CONTENT_HTML_MIN = 10;
    public static final int CONTENT_HTML_MAX = 1500000;

    public static final int CONTENT_TEXT_MIN = 10;
    public static final int CONTENT_TEXT_MAX = 30000;

    public static final int TAG_MAX_PER_POST = 5;

    //This specify how much score boost apply to certain field.
    public static final float SEARCH_TITLE_BOOST = 2.0f;
    public static final float SEARCH_CONTENT_BOOST = 1.0f;
    public static final float SEARCH_SUMMARY_BOOST = 1.5f;
}
