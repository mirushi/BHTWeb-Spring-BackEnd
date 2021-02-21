package com.bhtcnpm.website.constant.business.UserWebsite;

public class UWBusinessConstraint {
    public static final int MIN_USER_NAME_LENGTH = 5;
    public static final int MAX_USER_NAME_LENGTH = 128;
    public static final int MIN_DISPLAY_NAME_LENGTH = 5;
    public static final int MAX_DISPLAY_NAME_LENGTH = 128;
    public static final long MIN_REPUTATION_SCORE = 1L;
    public static final long MAX_REPUTATION_SCORE = Long.MAX_VALUE;
    public static final int MAX_AVATAR_URL_LENGTH = 2083;
    public static final int MAX_EMAIL_LENGTH = 255;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 2048;
}
