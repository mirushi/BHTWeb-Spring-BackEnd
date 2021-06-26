package com.bhtcnpm.website.security.evaluator.Post;

import com.bhtcnpm.website.constant.business.Post.HighlightPostBusinessConstant;
import com.bhtcnpm.website.constant.domain.Post.PostApprovalState;
import com.bhtcnpm.website.constant.domain.Post.PostBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.GenericOwnership;
import com.bhtcnpm.website.constant.security.evaluator.permission.HighlightPostPermissionRequest;
import com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.HighlightPostPermissionConstant;
import com.bhtcnpm.website.constant.security.permission.PostPermissionConstant;
import com.bhtcnpm.website.constant.security.permission.PostReportPermissionConstant;
import com.bhtcnpm.website.model.entity.PostEntities.HighlightPost;
import com.bhtcnpm.website.repository.Post.HighlightPostRepository;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.repository.Post.PostRepository;
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

    private final HighlightPostRepository highlightPostRepository;

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
        if (PostActionPermissionRequest.READ_PERMISSION.equals(permission)) {
            return this.checkPostReadPermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Edit.
        if (PostActionPermissionRequest.UPDATE_PERMISSION.equals(permission)) {
            return this.checkPostUpdatePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Delete.
        if (PostActionPermissionRequest.DELETE_PERMISSION.equals(permission)) {
            return this.checkPostDeletePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Save.
        if (PostActionPermissionRequest.SAVE_PERMISSION.equals(permission)) {
            return this.checkPostSavePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Like.
        if (PostActionPermissionRequest.LIKE_PERMISSION.equals(permission)) {
            return this.checkPostLikePermission(authentication, authenticatedUserID, state);
        }

        //Kiểm tra quyền Approve/Reject của user.
        if (PostActionPermissionRequest.APPROVE_PERMISSION.equals(permission)) {
            return this.checkPostApprovePermission(authentication, authenticatedUserID, approvalState);
        }

        //Kiểm tra quyền report của user.
        if (PostActionPermissionRequest.REPORT_PERMISSION.equals(permission)) {
            return this.checkPostReportPermission(authentication, authenticatedUserID, state);
        }

        //Kiểm tra quyền bình luận vào bài post của user.
        if (PostActionPermissionRequest.COMMENT_PERMISSION.equals(permission)) {
            return this.checkPostCommentPermission(authentication, authenticatedUserID, state);
        }

        //Kiểm tra quyền tạo Highlight post của user.
        if (HighlightPostPermissionRequest.HIGHLIGHT_POST_CREATE.equals(permission)) {
            return this.checkPostHighlightPermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền xoá một highlight post của user.
        if (HighlightPostPermissionRequest.HIGHLIGHT_POST_DELETE.equals(permission)) {
            return this.checkPostHighlightDeletePermission(authentication, authenticatedUserID, targetDomainObject);
        }

        //Kiểm tra quyền stickToTop của một highlight post của user.
        if (HighlightPostPermissionRequest.HIGHLIGHT_POST_STICKTOTOP.equals(permission)) {
            return this.checkPostHighlightStickToTop(authentication, authenticatedUserID, targetDomainObject);
        }

        throw new IllegalArgumentException(String.format("Post permission %s is not supported. Denying access to postID = %s", permission, targetDomainObject.getId()));
    }

    private boolean checkPostHighlightStickToTop (Authentication authentication, UUID authenticatedUserID, Post post) {
        //Bắt buộc phải có tài khoản mới được xoá highlight post.
        logger.info("Checking post highlight delete permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        if (SecurityUtils.containsAuthority(authentication, HighlightPostPermissionConstant.HIGHLIGHTPOST_PUBLIC_ALL_MANAGE)) {
            Optional<HighlightPost> object = highlightPostRepository.findByHighlightPostIdPost(post);
            if (object.isPresent()) {
                HighlightPost highlightPost = object.get();
                if (highlightPost.getRank() != HighlightPostBusinessConstant.RANK_MIN) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkPostHighlightDeletePermission(Authentication authentication, UUID authenticatedUserID, Post post) {
        //Bắt buộc phải có tài khoản mới được xoá highlight post.
        logger.info("Checking post highlight delete permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        if (SecurityUtils.containsAuthority(authentication, HighlightPostPermissionConstant.HIGHLIGHTPOST_PUBLIC_ALL_MANAGE)) {
            Optional<HighlightPost> object = highlightPostRepository.findByHighlightPostIdPost(post);
            if (object.isPresent()) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPostCommentPermission (Authentication authentication, UUID authenticatedUserID, PostBusinessState state) {
        //Bắt buộc phải có tài khoản mới được highlight post.
        logger.info("Checking post comment permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Post.
        if (PostBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, PostReportPermissionConstant.POSTREPORT_PUBLIC_ALL_CREATE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPostReportPermission (Authentication authentication, UUID authenticatedUserID, PostBusinessState state) {
        //Bắt buộc phải có tài khoản mới được highlight post.
        logger.info("Checking post save permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        if (PostBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, PostReportPermissionConstant.POSTREPORT_PUBLIC_ALL_CREATE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPostHighlightPermission (Authentication authentication, UUID authenticatedUserID, PostBusinessState state, Post post) {
        //Bắt buộc phải có tài khoản mới được highlight post.
        logger.info("Checking post save permission.");
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        if (PostBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, HighlightPostPermissionConstant.HIGHLIGHTPOST_PUBLIC_ALL_MANAGE)) {
                Optional<HighlightPost> highlightPost = highlightPostRepository.findByHighlightPostIdPost(post);
                if (highlightPost.isEmpty()) {
                    return true;
                }
            }
        }

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
            //Người có quyền xem bài viết UNLISTED có thể xem được bài viết này.
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
