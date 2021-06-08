package com.bhtcnpm.website.security.predicate.Post;

import com.bhtcnpm.website.constant.security.permission.PostPermissionConstant;
import com.bhtcnpm.website.security.util.SecurityUtils;
import org.springframework.security.core.Authentication;

public class PostAccessEvaluator {
    public static boolean hasAccessToPublicPost (Authentication authentication) {
        if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_PUBLIC_ALL_READ)
                || SecurityUtils.isAnonymousUser(authentication)) {
            return true;
        }
        return false;
    }

    public static boolean hasAccessToUnlistedPost (Authentication authentication) {
        if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_UNLISTED_ALL_READ)) {
            return true;
        }
        return false;
    }

    public static boolean hasAccessToDeletedPost (Authentication authentication) {
        if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_DELETED_ALL_READ)) {
            return true;
        }
        return false;
    }
}
