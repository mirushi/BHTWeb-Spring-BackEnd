package com.bhtcnpm.website.security.evaluator.Post;

import com.bhtcnpm.website.constant.domain.Post.PostApprovalState;
import com.bhtcnpm.website.constant.domain.Post.PostBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.GenericOwnership;
import com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.PostPermissionConstant;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.repository.PostRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
/*
* Please refer to Entity business state & permission (https://bhtcnpm.atlassian.net/wiki/spaces/BHTCNPM/pages/15990787/Entity+business+state+Permission)
* and permission list (https://bhtcnpm.atlassian.net/wiki/spaces/BHTCNPM/pages/13271066/Permission+List).
* Before attempting to modify below codes.
* */
public class PostPermissionEvaluator implements SimplePermissionEvaluator {

    private final PostRepository postRepository;

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        if (!(targetDomainObject instanceof Post)) {
            logger.warn(LogMessage.format("Target Domain Object type %s is not supported in PostPermissionEvaluator. Denying permission.",
                    targetDomainObject.getClass().getSimpleName().toUpperCase()));
            return false;
        }

        return checkPostPermission(authentication, (Post)targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        if (!(targetId instanceof Long)) {
            logger.warn(LogMessage.format("Target ID %s is not Long. Denying access.", targetId.toString()));
            return false;
        }

        Long targetIdLong = (Long)targetId;
        Optional<Post> object = postRepository.findById(targetIdLong);

        //Target ID does not correspond to any object. Therefore we don't grant any permission.
        if (object.isEmpty()) {
            return false;
        }

        Post entity = object.get();

        return checkPostPermission(authentication, entity, permission);
    }

    private boolean checkPostPermission(Authentication authentication, Post targetDomainObject, String permission) {
        //Kiểm tra xem Post hiện tại đang có state như thế nào.
        PostBusinessState state = targetDomainObject.getPostBusinessState();
        PostApprovalState approvalState = targetDomainObject.getPostApprovalState();

        UUID authenticatedUserID = SecurityUtils.getUserID(authentication);

        //State is null, the permission cannot be determined.
        if (state == null) {
            this.logger.warn(LogMessage.format("Post business state cannot be determined. Denying access to object %s", targetDomainObject.getId().toString()));
            return false;
        }

        //Kiểm tra quyền Read.
        if (PostInternalPermissionRequest.READ_PERMISSION.equals(permission)) {
            return this.checkPostReadPermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Edit.
        if (PostInternalPermissionRequest.UPDATE_PERMISSION.equals(permission)) {
            return this.checkPostUpdatePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Delete.
        if (PostInternalPermissionRequest.DELETE_PERMISSION.equals(permission)) {
            return this.checkPostDeletePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Save.
        if (PostInternalPermissionRequest.SAVE_PERMISSION.equals(permission)) {
            return this.checkPostSavePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Like.
        if (PostInternalPermissionRequest.LIKE_PERMISSION.equals(permission)) {
            return this.checkPostLikePermission(authentication, authenticatedUserID, state);
        }

        //Kiểm tra quyền Approve/Reject của user.
        if (PostInternalPermissionRequest.APPROVE_PERMISSION.equals(permission)) {
            return this.checkPostApprovePermission(authentication, authenticatedUserID, approvalState);
        }

        this.logger.warn(LogMessage.format("Post permission %s is not supported. Denying access to postID = %s", permission ,targetDomainObject.getId().toString()));
        return false;
    }

    private boolean checkPostApprovePermission (Authentication authentication, UUID authenticatedUserID, PostApprovalState approvalState) {
        //Bắt buộc phải có tài khoản mới được approve.
        logger.info("Checking post save permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state duyệt của Post.
        //Nếu post đang pending thì xét xem user có quyền để thực hiện approve/reject hay không.
        if (PostApprovalState.PENDING.equals(approvalState)) {
            if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_PENDING_ALL_APPROVE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPostLikePermission (Authentication authentication, UUID authenticatedUserID, PostBusinessState state) {
        //Bắt buộc phải có tài khoản mới được like.
        logger.info("Checking post save permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Post.
        if (PostBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_PUBLIC_ALL_LIKE)) {
                return true;
            }
        } else if (PostBusinessState.UNLISTED.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_UNLISTED_ALL_LIKE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPostSavePermission (Authentication authentication, UUID authenticatedUserID, PostBusinessState state, Post targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được save.
        logger.info("Checking post save permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Post.
        if (PostBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_PUBLIC_ALL_SAVE)) {
                return true;
            }
        } else if (PostBusinessState.UNLISTED.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, PostPermissionConstant.POST_UNLISTED_ALL_SAVE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPostDeletePermission (Authentication authentication, UUID authenticatedUserID, PostBusinessState state, Post targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được deleted.
        logger.info("Checking post update permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Post.
        if (PostBusinessState.PUBLIC.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication,
                    targetDomainObject, PostPermissionConstant.POST_PUBLIC_SELF_DELETE)) {
                return true;
            }
            else if (SecurityUtils.containsAuthority(authentication,
                    com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_PUBLIC_ALL_DELETE)) {
                return true;
            }
            return false;
        }
        else if (PostBusinessState.UNLISTED.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject,
                    com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_UNLISTED_SELF_DELETE)) {
                return true;
            }
            else if (SecurityUtils.containsAuthority(authentication,
                    com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_UNLISTED_ALL_DELETE)) {
                return true;
            }
            return false;
        }
        else if (PostBusinessState.DELETED.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_DELETED_ALL_DELETE)) {
                return true;
            }
            return false;
        }

        return false;
    }

    private boolean checkPostUpdatePermission (Authentication authentication, UUID authenticatedUserID, PostBusinessState state, Post targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được edit.
        logger.info("Checking post update permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }
        //Xét state của Post.
        if (PostBusinessState.PUBLIC.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject, com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_PUBLIC_SELF_UPDATE)) {
                return true;
            }
            else if (SecurityUtils.containsAuthority(authentication, com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_PUBLIC_ALL_UPDATE)) {
                return true;
            }
            return false;
        }
        else if (PostBusinessState.UNLISTED.equals(state)){
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject , com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_UNLISTED_SELF_UPDATE)) {
                return true;
            }
            else if (SecurityUtils.containsAuthority(authentication, com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_UNLISTED_ALL_UPDATE)) {
                return true;
            }
            return false;
        }
        else if (PostBusinessState.DELETED.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_DELETED_ALL_UPDATE)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean checkPostReadPermission (Authentication authentication, UUID authenticatedUserID, PostBusinessState state, Post targetDomainObject) {
        //Check tương ứng với từng PostBusinessState.
        if (PostBusinessState.PUBLIC.equals(state)) {
            return true;
        }
        else if (PostBusinessState.UNLISTED.equals(state)) {
            //Tác giả của bài viết có thể xem bài viết của chính mình.
            if (authenticatedUserID == null) {
                logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
                return false;
            }
            if (targetDomainObject.getAuthor().getId().equals(authenticatedUserID)) {
                return true;
            }
            //Người có quyền xem bài viết PRIVATE có thể xem được bài viết này.
            if (SecurityUtils.containsAuthority(authentication, com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_UNLISTED_ALL_READ)) {
                return true;
            }
            return false;
        }
        else if (PostBusinessState.DELETED.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, com.bhtcnpm.website.constant.security.permission.PostPermissionConstant.POST_DELETED_ALL_READ)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean isOwnerAndContainsAuthority (Authentication authentication, Post targetDomainObject, String permission) {
        UUID authenticatedUserID = SecurityUtils.getUserID(authentication);
        if (GenericOwnership.OWNER.equals(getOwnership(authenticatedUserID, targetDomainObject))
                && SecurityUtils.containsAuthority(authentication, permission)) {
            return true;
        }
        return false;
    }

    private GenericOwnership getOwnership (UUID userID, Post post) {
        if (post.getAuthor().getId().equals(userID)){
            return GenericOwnership.OWNER;
        }
        return GenericOwnership.NONE;
    }
}
