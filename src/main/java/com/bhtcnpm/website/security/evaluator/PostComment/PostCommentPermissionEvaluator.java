package com.bhtcnpm.website.security.evaluator.PostComment;

import com.bhtcnpm.website.constant.domain.PostComment.PostCommentBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.GenericOwnership;
import com.bhtcnpm.website.constant.security.evaluator.permission.PostCommentActionPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.PostCommentPermissionConstant;
import com.bhtcnpm.website.model.entity.PostCommentEntities.PostComment;
import com.bhtcnpm.website.repository.PostCommentRepository;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
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
public class PostCommentPermissionEvaluator implements SimplePermissionEvaluator {

    private final PostCommentRepository postCommentRepository;

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        if (!(targetDomainObject instanceof PostComment)) {
            logger.warn(LogMessage.format("Target Domain Object type %s is not supported in PostCommentPermissionEvaluator. Denying permission.",
                    targetDomainObject.getClass().getSimpleName().toUpperCase()));
            return false;
        }

        return checkPostPermission(authentication, (PostComment) targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        if (!(targetId instanceof Long)) {
            logger.warn(LogMessage.format("Target ID %s is not Long. Denying access .", targetId.toString()));
            return false;
        }

        Long targetIDLong = (Long)targetId;
        Optional<PostComment> object = postCommentRepository.findById(targetIDLong);

        //Target ID does not correspond to any object. Therefore we don't grant any permission.
        if (object.isEmpty()) {
            return false;
        }

        PostComment entity = object.get();

        return checkPostPermission(authentication, entity, permission);
    }

    private boolean checkPostPermission (Authentication authentication, PostComment targetDomainObject, String permission) {
        //Kiểm tra xem PostComment hiện tại đang có state như thế nào.
        PostCommentBusinessState state = targetDomainObject.getPostCommentBusinessState();

        UUID authenticatedUserID = SecurityUtils.getUserID(authentication);

        //State is null, the permission cannot be determined.
        if (state == null) {
            this.logger.warn(LogMessage.format("Post comment business state cannot be determined. Denying access to object %s", targetDomainObject.getId().toString()));
            return false;
        }

        //Kiểm tra quyền Edit.
        if (PostCommentActionPermissionRequest.UPDATE_PERMISSION.equals(permission)) {
            return checkPostUpdatePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Delete.
        if (PostCommentActionPermissionRequest.DELETE_PERMISSION.equals(permission)) {
            return checkPostDeletePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Reply.
        if (PostCommentActionPermissionRequest.REPLY_PERMISSION.equals(permission)) {
            return checkPostReplyPermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Like.
        if (PostCommentActionPermissionRequest.LIKE_PERMISSION.equals(permission)) {
            return checkLikePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Read.
        if (PostCommentActionPermissionRequest.READ_PERMISSION.equals(permission)) {
            return checkReadPermission(state);
        }

        throw new IllegalArgumentException("Post comment request permission is not supported.");
    }

    private boolean checkReadPermission (PostCommentBusinessState state) {
        if (PostCommentBusinessState.PUBLIC.equals(state)) {
            return true;
        }
        if (PostCommentBusinessState.DELETE.equals(state)) {
            return false;
        }

        return false;
    }

    private boolean checkLikePermission (Authentication authentication,
                                              UUID authenticatedUserID,
                                              PostCommentBusinessState state,
                                              PostComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được reply.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Post Comment.
        if (PostCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, PostCommentPermissionConstant.POSTCOMMENT_PUBLIC_ALL_LIKE)) {
                return true;
            }
        }

        if (PostCommentBusinessState.DELETE.equals(state)) {
            return false;
        }

        return false;
    }

    private boolean checkPostReplyPermission (Authentication authentication,
                                              UUID authenticatedUserID,
                                              PostCommentBusinessState state,
                                              PostComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được reply.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Post Comment.
        if (PostCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, PostCommentPermissionConstant.POSTCOMMENT_PUBLIC_SELF_CREATE)) {
                return true;
            }
        }

        if (PostCommentBusinessState.DELETE.equals(state)) {
            return false;
        }

        return false;
    }

    private boolean checkPostDeletePermission (Authentication authentication,
                                               UUID authenticatedUserID,
                                               PostCommentBusinessState state,
                                               PostComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được delete.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Post Comment.
        if (PostCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, PostCommentPermissionConstant.POSTCOMMENT_PUBLIC_ALL_DELETE)) {
                return true;
            }

            if (isOwnerAndContainsAuthority(authentication, targetDomainObject,
                    PostCommentPermissionConstant.POSTCOMMENT_PUBLIC_SELF_DELETE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPostUpdatePermission (Authentication authentication,
                                             UUID authenticatedUserID,
                                             PostCommentBusinessState state,
                                             PostComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được edit.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Post Comment.
        if (PostCommentBusinessState.PUBLIC.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject,
                    PostCommentPermissionConstant.POSTCOMMENT_PUBLIC_SELF_UPDATE)) {
                return true;
            }
            return false;
        }

        return false;
    }

    private boolean isOwnerAndContainsAuthority (Authentication authentication, PostComment targetDomainObject, String permission) {
        UUID authenticatedUserID = SecurityUtils.getUserID(authentication);
        if (GenericOwnership.OWNER.equals(getOwnership(authenticatedUserID, targetDomainObject))
                && SecurityUtils.containsAuthority(authentication, permission)) {
            return true;
        }
        return false;
    }

    private GenericOwnership getOwnership (UUID userID, PostComment postComment) {
        if (postComment.getAuthor().getId().equals(userID)) {
            return GenericOwnership.OWNER;
        }
        return GenericOwnership.NONE;
    }

}
