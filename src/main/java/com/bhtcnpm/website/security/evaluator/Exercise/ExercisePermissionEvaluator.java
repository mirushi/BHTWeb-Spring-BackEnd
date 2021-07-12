package com.bhtcnpm.website.security.evaluator.Exercise;

import com.bhtcnpm.website.constant.domain.Exercise.ExerciseApprovalState;
import com.bhtcnpm.website.constant.domain.Exercise.ExerciseBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseActionPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.ExercisePermissionConstant;
import com.bhtcnpm.website.constant.security.permission.ExerciseReportPermissionConstant;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExercisePermissionEvaluator implements SimplePermissionEvaluator {

    private final ExerciseRepository exerciseRepository;

    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        if (!(targetDomainObject instanceof Exercise)) {
            log.warn(String.format("Target domain object type %s is not supported in ExerciseCommentPermissionEvaluator. Denying permission.",
                    targetDomainObject.getClass().getSimpleName().toUpperCase()));
            return false;
        }
        return checkExercisePermission(authentication, (Exercise) targetDomainObject, permission);
    }

    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        if (!(targetId instanceof Long)) {
            log.warn(String.format("Target ID %s is not Long. Denying access .", targetId.toString()));
            return false;
        }

        Long targetIDLong = (Long) targetId;
        Optional<Exercise> object = exerciseRepository.findById(targetIDLong);

        //Target ID does not correspond to any object. Therefore we don't grant any permission.
        if (object.isEmpty()) {
            return false;
        }

        Exercise entity = object.get();

        return checkExercisePermission(authentication, entity, permission);
    }

    private boolean checkExercisePermission (Authentication authentication, Exercise targetDomainObject, String permission) {
        //Kiểm tra xem Exercise hiện tại đang có state như thế nào.
        ExerciseBusinessState state = targetDomainObject.getExerciseBusinessState();
        ExerciseApprovalState approvalState = targetDomainObject.getExerciseApprovalState();

        UUID authenticatedUserID = SecurityUtils.getUserID(authentication);

        if (state == null) {
            return false;
        }

        //Kiểm tra quyền read.
        if (ExerciseActionPermissionRequest.READ_PERMISSION.equals(permission)) {
            return this.checkExerciseReadPermission(authentication, authenticatedUserID, targetDomainObject, state, permission);
        }

        //Kiểm tra quyền update.
        if (ExerciseActionPermissionRequest.UPDATE_PERMISSION.equals(permission)) {
            return this.checkExerciseUpdatePermission(authentication, authenticatedUserID, targetDomainObject, state, permission);
        }

        //Kiểm tra quyền delete.
        if (ExerciseActionPermissionRequest.DELETE_PERMISSION.equals(permission)) {
            return this.checkExerciseDeletePermission(authentication, authenticatedUserID, targetDomainObject, state, permission);
        }

        //Kiểm tra quyền report của user.
        if (ExerciseActionPermissionRequest.REPORT_PERMISSION.equals(permission)) {
            return this.checkExerciseReportPermission(authentication, authenticatedUserID, targetDomainObject, state, permission);
        }

        throw new IllegalArgumentException(String.format("Exercise permission %s is not supported. Denying access to exerciseID = %s", permission, targetDomainObject.getId()));
    }

    private boolean checkExerciseDeletePermission (Authentication authentication, UUID authenticatedUserID, Exercise targetDomainObject, ExerciseBusinessState state, String permission) {
        if (authenticatedUserID == null) {
            return false;
        }
        //Xét state của Exercise.
        if (ExerciseBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, ExercisePermissionConstant.EXERCISE_ALL_ALL_DELETE)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean checkExerciseUpdatePermission (Authentication authentication, UUID authenticatedUserID, Exercise targetDomainObject, ExerciseBusinessState state, String permission) {
        //Bắt buộc phải có tài khoản mới được update.
        if (authenticatedUserID == null) {
            return false;
        }
        //Xét state của exercise.
        if (ExerciseBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, ExercisePermissionConstant.EXERCISE_ALL_ALL_UPDATE)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean checkExerciseReadPermission (Authentication authentication, UUID authenticatedUserID, Exercise targetDomainObject, ExerciseBusinessState state, String permission) {
        //Check tương ứng với từng ExerciseBusinessState.
        if (ExerciseBusinessState.PUBLIC.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean checkExerciseReportPermission (Authentication authentication, UUID authenticatedUserID ,Exercise targetDomainObject, ExerciseBusinessState state ,String permission) {
        if (authenticatedUserID == null) {
            return false;
        }

        if (ExerciseBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, ExerciseReportPermissionConstant.EXERCISEREPORT_PUBLIC_ALL_CREATE)) {
                return true;
            }
        }

        return false;
    }
}
