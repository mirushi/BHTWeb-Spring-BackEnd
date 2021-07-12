package com.bhtcnpm.website.security.evaluator.DocComment;

import com.bhtcnpm.website.constant.business.DocComment.DocCommentActionAvailableConstant;
import com.bhtcnpm.website.constant.business.DocComment.DocCommentBusinessConstant;
import com.bhtcnpm.website.constant.security.evaluator.GenericOwnership;
import com.bhtcnpm.website.constant.security.evaluator.permission.DocCommentActionPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.DocCommentPermissionConstant;
import com.bhtcnpm.website.constant.security.permission.DocCommentReportPermissionConstant;
import com.bhtcnpm.website.model.entity.DocCommentEntities.DocComment;
import com.bhtcnpm.website.model.entity.DocCommentEntities.DocCommentBusinessState;
import com.bhtcnpm.website.model.entity.PostCommentEntities.PostComment;
import com.bhtcnpm.website.repository.Doc.DocCommentRepository;
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
public class DocCommentPermissionEvaluator implements SimplePermissionEvaluator {
    private final DocCommentRepository docCommentRepository;
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        if (!(targetDomainObject instanceof DocComment)) {
            logger.warn(String.format("Target Domain Object type %s is not supported in DocCommentPermissionEvaluator. Denying permission.",
                    targetDomainObject.getClass().getSimpleName().toUpperCase()));
            return false;
        }
        return checkDocCommentPermission(authentication, (DocComment) targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        if (!(targetId instanceof Long)) {
            logger.warn(String.format("Target ID %s is not Long. Denying access .", targetId.toString()));
            return false;
        }

        Long targetIDLong = (Long) targetId;
        Optional<DocComment> object = docCommentRepository.findById(targetIDLong);

        //Target ID does not correspond to any object. Therefore we don't grant any permission.
        if (object.isEmpty()) {
            return false;
        }

        DocComment entity = object.get();

        return checkDocCommentPermission(authentication, entity, permission);
    }

    private boolean checkDocCommentPermission(Authentication authentication, DocComment targetDomainObject, String permission) {
        //Kiểm tra xem DocComment hiện tại đang có state như thế nào.
        DocCommentBusinessState state = targetDomainObject.getDocCommentBusinessState();

        UUID authenticatedUserId = SecurityUtils.getUserID(authentication);

        //State is null, the permission cannot be determined.
        if (state == null) {
            this.logger.warn(LogMessage.format("Doc comment business state cannot be determined. Denying access to object %s", targetDomainObject.getId().toString()));
            return false;
        }

        //Kiểm tra quyền Edit.
        if (DocCommentActionPermissionRequest.UPDATE_PERMISSION.equals(permission)) {
            return this.checkDocCommentUpdatePermission(authentication, authenticatedUserId, state, targetDomainObject);
        }

        //Kiểm tra quyền Delete.
        if (DocCommentActionPermissionRequest.DELETE_PERMISSION.equals(permission)) {
            return this.checkDocCommentDeletePermission(authentication, authenticatedUserId, state, targetDomainObject);
        }

        //Kiểm tra quyền Reply.
        if (DocCommentActionPermissionRequest.REPLY_PERMISSION.equals(permission)) {
            return this.checkDocCommentReplyPermission(authentication, authenticatedUserId, state, targetDomainObject);
        }

        //Kiểm tra quyền Like.
        if (DocCommentActionPermissionRequest.LIKE_PERMISSION.equals(permission)) {
            return this.checkDocCommentLikePermission(authentication, authenticatedUserId, state, targetDomainObject);
        }

        //Kiểm tra quyền Read.
        if (DocCommentActionPermissionRequest.READ_PERMISSION.equals(permission)) {
            return this.checkDocCommentReadPermission(authentication, authenticatedUserId, state, targetDomainObject);
        }

        //Kiểm tra quyền Report.
        if (DocCommentActionPermissionRequest.REPORT_PERMISSION.equals(permission)) {
            return this.checkDocCommentReportPermission(authentication, authenticatedUserId, state, targetDomainObject);
        }

        throw new IllegalArgumentException(String.format("Doc comment request permission %s is not supported.", permission));
    }

    private boolean checkDocCommentReportPermission (Authentication authentication, UUID authenticatedUserID, DocCommentBusinessState state, DocComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được reply.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Doc Comment.
        if (DocCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, DocCommentReportPermissionConstant.DOCCOMMENTREPORT_PUBLIC_ALL_CREATE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkDocCommentReadPermission (Authentication authentication, UUID authenticatedUserID, DocCommentBusinessState state, DocComment targetDomainObject) {
        if (DocCommentBusinessState.PUBLIC.equals(state)) {
            return true;
        }
        if (DocCommentBusinessState.DELETE.equals(state)) {
            return false;
        }

        return false;
    }

    private boolean checkDocCommentLikePermission (Authentication authentication, UUID authenticatedUserID, DocCommentBusinessState state, DocComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được like.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Doc Comment.
        if (DocCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, DocCommentPermissionConstant.DOCCOMMENT_PUBLIC_ALL_LIKE)) {
                return true;
            }
        }

        if (DocCommentBusinessState.DELETE.equals(state)) {
            return false;
        }

        return false;
    }

    private boolean checkDocCommentReplyPermission (Authentication authentication, UUID authenticatedUserID, DocCommentBusinessState state, DocComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được reply.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Doc comment.
        if (DocCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, DocCommentPermissionConstant.DOCCOMMENT_PUBLIC_SELF_CREATE)) {
                return true;
            }
        }

        if (DocCommentBusinessState.DELETE.equals(state)) {
            return false;
        }

        return false;
    }

    private boolean checkDocCommentDeletePermission (Authentication authentication, UUID authenticatedUserID, DocCommentBusinessState state, DocComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được delete.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Doc Comment.
        if (DocCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication,
                    DocCommentPermissionConstant.DOCCOMMENT_PUBLIC_ALL_DELETE)) {
                return true;
            }

            if (isOwnerAndContainsAuthority(authentication, targetDomainObject,
                    DocCommentPermissionConstant.DOCCOMMENT_PUBLIC_SELF_DELETE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkDocCommentUpdatePermission (Authentication authentication, UUID authenticatedUserId, DocCommentBusinessState state, DocComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được edit.
        if (authenticatedUserId == null) {
            logger.warn("User ID not found in authentication object. Denying access.");
            return false;
        }

        //Xét state của Doc Comment.
        if (DocCommentBusinessState.PUBLIC.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject, DocCommentPermissionConstant.DOCCOMMENT_PUBLIC_SELF_UPDATE)) {
                return true;
            }
            return false;
        }

        return false;
    }

    private boolean isOwnerAndContainsAuthority (Authentication authentication, DocComment targetDomainObject, String permission) {
        UUID authenticatedUserId = SecurityUtils.getUserID(authentication);
        if (GenericOwnership.OWNER.equals(getOwnership(authenticatedUserId, targetDomainObject))
                && SecurityUtils.containsAuthority(authentication, permission)) {
            return true;
        }

        return false;
    }

    private GenericOwnership getOwnership (UUID userID, DocComment docComment) {
        if (docComment.getAuthor().getId().equals(userID)) {
            return GenericOwnership.OWNER;
        }
        return GenericOwnership.NONE;
    }

}
