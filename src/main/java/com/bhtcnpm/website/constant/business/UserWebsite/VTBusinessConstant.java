package com.bhtcnpm.website.constant.business.UserWebsite;

/* Constants for Email Verification Token */
public class VTBusinessConstant {
    public static final int MAIL_VERIFY_TOKEN_EXPIRATION_TIME = 60 * 8; //Token expires after 8 hours.
    public static final int MAIL_FORGOT_PASSWORD_TOKEN_EXPIRATION_TIME = 60 * 8; //Token expires after 8 hours.
    public static final int MAIL_FORGOT_PASSWORD_VERIFY_TOKEN_NUM_CHAR = 52;
    public static final int MAIL_VERIFY_TOKEN_NUM_CHAR = 52;
    public static final int BIT_PER_CHAR = 5;
}
