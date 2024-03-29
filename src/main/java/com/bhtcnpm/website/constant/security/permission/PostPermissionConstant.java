package com.bhtcnpm.website.constant.security.permission;

public class PostPermissionConstant {
    //Read permissions.
    public static final String POST_PUBLIC_ALL_READ = "ROLE_POST_PUBLIC_ALL_READ";
    public static final String POST_UNLISTED_ALL_READ = "ROLE_POST_UNLISTED_ALL_READ";
    public static final String POST_DELETED_ALL_READ = "ROLE_POST_DELETED_ALL_READ";

    //Update permissions.
    public static final String POST_PUBLIC_SELF_UPDATE = "ROLE_POST_PUBLIC_SELF_UPDATE";
    public static final String POST_UNLISTED_SELF_UPDATE = "ROLE_POST_UNLISTED_SELF_UPDATE";
    public static final String POST_PUBLIC_ALL_UPDATE = "ROLE_POST_PUBLIC_ALL_UPDATE";
    public static final String POST_UNLISTED_ALL_UPDATE = "ROLE_POST_UNLISTED_ALL_UPDATE";
    public static final String POST_DELETED_ALL_UPDATE = "ROLE_POST_DELETED_ALL_UPDATE";

    //Delete permissions.
    public static final String POST_PUBLIC_SELF_DELETE = "ROLE_POST_PUBLIC_SELF_DELETE";
    public static final String POST_UNLISTED_SELF_DELETE = "ROLE_POST_UNLISTED_SELF_DELETE";
    public static final String POST_DELETED_SELF_DELETE = "ROLE_POST_DELETED_SELF_DELETE";
    public static final String POST_PUBLIC_ALL_DELETE = "ROLE_POST_PUBLIC_ALL_DELETE";
    public static final String POST_UNLISTED_ALL_DELETE = "ROLE_POST_UNLISTED_ALL_DELETE";
    public static final String POST_DELETED_ALL_DELETE = "ROLE_POST_DELETED_ALL_DELETE";

    //Create permissions.
    public static final String POST_PENDING_SELF_CREATE = "ROLE_POST_PENDING_SELF_CREATE";

    //Save permissions.
    public static final String POST_PUBLIC_ALL_SAVE = "ROLE_POST_PUBLIC_ALL_SAVE";
    public static final String POST_UNLISTED_ALL_SAVE = "ROLE_POST_UNLISTED_ALL_SAVE";

    //Like permission.
    public static final String POST_PUBLIC_ALL_LIKE = "ROLE_POST_PUBLIC_ALL_LIKE";
    public static final String POST_UNLISTED_ALL_LIKE = "ROLE_POST_UNLISTED_ALL_LIKE";

    //Approval permission.
    public static final String POST_PENDING_ALL_APPROVE = "ROLE_POST_PENDING_ALL_APPROVE";
}
