package com.bhtcnpm.website.security.evaluator.Post;

import com.bhtcnpm.website.model.entity.PostEntities.HighlightPost;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class HighlightPostPermissionEvaluator implements SimplePermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        throw new NotImplementedException();
    }

    private boolean checkHighlightPostPermission (Authentication authentication, HighlightPost targetDomainObject) {
        throw new NotImplementedException();
    }

}
