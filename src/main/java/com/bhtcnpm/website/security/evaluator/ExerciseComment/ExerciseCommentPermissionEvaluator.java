package com.bhtcnpm.website.security.evaluator.ExerciseComment;

import com.bhtcnpm.website.constant.domain.ExerciseComment.ExerciseCommentBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.GenericOwnership;
import com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseCommentActionPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.ExerciseCommentPermissionConstant;
import com.bhtcnpm.website.constant.security.permission.ExerciseCommentReportPermissionConstant;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseComment;
import com.bhtcnpm.website.repository.ExerciseComment.ExerciseCommentRepository;
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
public class ExerciseCommentPermissionEvaluator implements SimplePermissionEvaluator {
    private final ExerciseCommentRepository exerciseCommentRepository;
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        if (!(targetDomainObject instanceof ExerciseComment)) {
            logger.warn(LogMessage.format("Target domain object type %s is not supported in ExerciseCommentPermissionEvaluator. Denying permission.",
                    targetDomainObject.getClass().getSimpleName().toUpperCase()));
            return false;
        }
        return checkExerciseCommentPermission(authentication, (ExerciseComment) targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        if (!(targetId instanceof Long)) {
            logger.warn(LogMessage.format("Target ID %s is not Long. Denying access .", targetId.toString()));
            return false;
        }

        Long targetIDLong = (Long) targetId;
        Optional<ExerciseComment> object = exerciseCommentRepository.findById(targetIDLong);

        //Target ID does not correspond to any object. Therefore we don't grant any permission.
        if (object.isEmpty()) {
            return false;
        }

        ExerciseComment entity = object.get();

        return checkExerciseCommentPermission(authentication, entity, permission);
    }

    private boolean checkExerciseCommentPermission (Authentication authentication, ExerciseComment targetDomainObject, String permission) {
        //Kiểm tra xem ExerciseComment hiện tại đang có state như thế nào.
        ExerciseCommentBusinessState state = targetDomainObject.getExerciseCommentBusinessState();

        UUID authenticatedUserID = SecurityUtils.getUserID(authentication);

        //State is null, the permission cannot be determined.
        if (state == null) {
            this.logger.warn(LogMessage.format("Exercise comment business state cannot be determined. Denying access to object %s", targetDomainObject.getId().toString()));
            return false;
        }

        //Kiểm tra quyền Edit.
        if (ExerciseCommentActionPermissionRequest.UPDATE_PERMISSION.equals(permission)) {
            return checkExerciseCommentUpdatePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Delete.
        if (ExerciseCommentActionPermissionRequest.DELETE_PERMISSION.equals(permission)) {
            return checkExerciseCommentDeletePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Reply.
        if (ExerciseCommentActionPermissionRequest.REPLY_PERMISSION.equals(permission)) {
            return checkExerciseCommentReplyPermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Like.
        if (ExerciseCommentActionPermissionRequest.LIKE_PERMISSION.equals(permission)) {
            return checkExerciseCommentLikePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Read.
        if (ExerciseCommentActionPermissionRequest.READ_PERMISSION.equals(permission)) {
            return checkExerciseCommentReadPermission(state);
        }

        //Kiểm tra quyền Report.
        if (ExerciseCommentActionPermissionRequest.REPORT_PERMISSION.equals(permission)) {
            return checkExerciseCommentReportPermission(authentication, authenticatedUserID, state);
        }

        throw new IllegalArgumentException("Exercise comment request permission is not supported.");
    }

    private boolean checkExerciseCommentReportPermission (Authentication authentication, UUID authenticatedUserID, ExerciseCommentBusinessState state) {
        //Bắt buộc phải có tài khoản mới được reply.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Exercise Comment.
        if (ExerciseCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, ExerciseCommentReportPermissionConstant.EXERCISECOMMENTREPORT_PUBLIC_ALL_CREATE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkExerciseCommentReadPermission (ExerciseCommentBusinessState state) {
        if (ExerciseCommentBusinessState.PUBLIC.equals(state)) {
            return true;
        }
        if (ExerciseCommentBusinessState.DELETE.equals(state)) {
            return false;
        }
        return false;
    }

    private boolean checkExerciseCommentLikePermission (Authentication authentication,
                                                        UUID authenticatedUserID,
                                                        ExerciseCommentBusinessState state,
                                                        ExerciseComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được like.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Exercise Comment.
        if (ExerciseCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, ExerciseCommentPermissionConstant.EXERCISECOMMENT_PUBLIC_ALL_LIKE)) {
                return true;
            }
        }

        if (ExerciseCommentBusinessState.DELETE.equals(state)) {
            return false;
        }

        return false;
    }

    private boolean checkExerciseCommentReplyPermission (Authentication authentication,
                                                         UUID authenticatedUserID,
                                                         ExerciseCommentBusinessState state,
                                                         ExerciseComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được reply.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Exercise Comment.
        if (ExerciseCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, ExerciseCommentPermissionConstant.EXERCISECOMMENT_PUBLIC_SELF_CREATE)) {
                return true;
            }
        }

        if (ExerciseCommentBusinessState.DELETE.equals(state)) {
            return false;
        }

        return false;
    }

    private boolean checkExerciseCommentDeletePermission (Authentication authentication, UUID authenticatedUserID, ExerciseCommentBusinessState state, ExerciseComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được delete.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Exercise Comment.
        if (ExerciseCommentBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, ExerciseCommentPermissionConstant.EXERCISECOMMENT_PUBLIC_ALL_DELETE)) {
                return true;
            }

            if (isOwnerAndContainsAuthority(authentication, targetDomainObject,
                    ExerciseCommentPermissionConstant.EXERCISECOMMENT_PUBLIC_SELF_DELETE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkExerciseCommentUpdatePermission (Authentication authentication, UUID authenticatedUserID, ExerciseCommentBusinessState state, ExerciseComment targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được edit.
        if (authenticatedUserID == null) {
            logger.warn(LogMessage.format("User ID not found in authentication object. Denying access."));
            return false;
        }

        //Xét state của Exercise Comment.
        if (ExerciseCommentBusinessState.PUBLIC.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject,
                    ExerciseCommentPermissionConstant.EXERCISECOMMENT_PUBLIC_SELF_UPDATE)) {
                return true;
            }
            return false;
        }

        return false;
    }

    private boolean isOwnerAndContainsAuthority (Authentication authentication, ExerciseComment targetDomainObject, String permission) {
        UUID authenticatedUserID = SecurityUtils.getUserID(authentication);
        if (GenericOwnership.OWNER.equals(getOwnership(authenticatedUserID, targetDomainObject))
                && SecurityUtils.containsAuthority(authentication, permission)) {
            return true;
        }
        return false;
    }

    private GenericOwnership getOwnership (UUID userID, ExerciseComment exerciseComment) {
        if (exerciseComment.getAuthor().getId().equals(userID)) {
            return GenericOwnership.OWNER;
        }
        return GenericOwnership.NONE;
    }

}
