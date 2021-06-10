package com.bhtcnpm.website.security.evaluator.Post;

import com.bhtcnpm.website.model.entity.PostEntities.HighlightPost;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class HighlightPostPermissionEvaluator implements SimplePermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        return false;
    }

    private boolean checkHighlightPostPermission (Authentication authentication, HighlightPost targetDomainObject) {
        return false;
    }

}
