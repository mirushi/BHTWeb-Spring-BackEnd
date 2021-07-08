package com.bhtcnpm.website.security.evaluator.Exercise;

import com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseActionPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.ExerciseReportPermissionConstant;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

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
        if (ExerciseActionPermissionRequest.REPORT_PERMISSION.equals(permission)) {
            if (SecurityUtils.containsAuthority(authentication, ExerciseReportPermissionConstant.EXERCISEREPORT_PUBLIC_ALL_CREATE)) {
                return true;
            }
        }

        throw new IllegalArgumentException(String.format("Exercise permission %s is not supported. Denying access to exerciseID = %s", permission, targetDomainObject.getId()));
    }



}
