package com.bhtcnpm.website.security.evaluator;

import com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant;
import com.bhtcnpm.website.security.evaluator.Post.PostPermissionEvaluator;
import com.bhtcnpm.website.security.evaluator.PostComment.PostCommentPermissionEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class EntityPermissionEvaluator implements PermissionEvaluator {

    private final PostPermissionEvaluator postPermissionEvaluator;
    private final PostCommentPermissionEvaluator postCommentPermissionEvaluator;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
        String permissionString = (String)permission;

        switch (targetType) {
            case ObjectTypeConstant.POST_OBJECT: {
                return postPermissionEvaluator.hasPermission(authentication, targetDomainObject, permissionString);
            }
            case ObjectTypeConstant.POSTCOMMENT_OBJECT: {
                return postCommentPermissionEvaluator.hasPermission(authentication, targetDomainObject, permissionString);
            }
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if ((authentication == null) || (targetId == null) || !(permission instanceof String)) {
            return false;
        }
        String permissionString = (String)permission;

        switch (targetType) {
            case ObjectTypeConstant.POST_OBJECT: {
                return postPermissionEvaluator.hasPermission(authentication, targetId, permissionString);
            }
            case ObjectTypeConstant.POSTCOMMENT_OBJECT: {
                return postCommentPermissionEvaluator.hasPermission(authentication, targetId, permissionString);
            }
        }
        return false;
    }
}
