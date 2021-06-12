package com.bhtcnpm.website.constant.domain.Post;

import com.bhtcnpm.website.constant.business.GenericBusinessConstant;
import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;

public class PostDomainConstant {
    public static final int TITLE_LENGTH = PostBusinessConstant.TITLE_MAX;
    public static final int SUMMARY_LENGTH = PostBusinessConstant.SUMMARY_MAX;
    public static final int IMAGEURL_LENGTH = GenericBusinessConstant.URL_MAX_LENGTH;
    public static final int CONTENT_PLAIN_TEXT_LENGTH = PostBusinessConstant.CONTENT_TEXT_MAX;
}
