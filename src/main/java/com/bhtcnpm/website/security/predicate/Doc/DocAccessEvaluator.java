package com.bhtcnpm.website.security.predicate.Doc;

import com.bhtcnpm.website.constant.security.permission.DocPermissionConstant;
import com.bhtcnpm.website.security.util.SecurityUtils;
import org.springframework.security.core.Authentication;

public class DocAccessEvaluator {
    public static boolean hasAccessToPublicDoc (Authentication authentication) {
        if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_PUBLIC_ALL_READ)
                || SecurityUtils.isAnonymousUser(authentication)) {
            return true;
        }
        return false;
    }

    public static boolean hasAccessToUnlistedDoc (Authentication authentication) {
        if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_UNLISTED_ALL_READ)) {
            return true;
        }
        return false;
    }

    public static boolean hasAccessToDeletedDoc (Authentication authentication) {
        if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_DELETED_ALL_READ)) {
            return true;
        }
        return false;
    }

}
