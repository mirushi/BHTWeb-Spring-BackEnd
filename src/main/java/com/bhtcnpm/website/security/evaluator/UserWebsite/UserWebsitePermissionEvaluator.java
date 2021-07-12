package com.bhtcnpm.website.security.evaluator.UserWebsite;

import com.bhtcnpm.website.constant.security.evaluator.permission.UserWebsiteActionPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.UserWebsitePermissionConstant;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
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
public class UserWebsitePermissionEvaluator implements SimplePermissionEvaluator {

    private final UserWebsiteRepository userWebsiteRepository;

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        if (!(targetDomainObject instanceof UserWebsite)) {
            logger.warn(LogMessage.format("Target Domain Object type %s is not supported in PostPermissionEvaluator. Denying permission.",
                    targetDomainObject.getClass().getSimpleName().toUpperCase()));
            return false;
        }

        return checkUserWebsitePermission(authentication, (UserWebsite) targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        if (!(targetId instanceof UUID)) {
            logger.warn(LogMessage.format("Target ID %s is not UUID. Denying access.", targetId.toString()));
            return false;
        }

        UUID targetUserID = (UUID) targetId;
        Optional<UserWebsite> object = userWebsiteRepository.findById(targetUserID);

        if (object.isEmpty()) {
            return false;
        }

        UserWebsite entity = object.get();

        return checkUserWebsitePermission(authentication, entity, permission);
    }

    private boolean checkUserWebsitePermission (Authentication authentication, UserWebsite targetDomainObject, String permission) {
        UUID currentUserID = SecurityUtils.getUserID(authentication);

        //Kiểm tra quyền update.
        if (UserWebsiteActionPermissionRequest.UPDATE_PERMISSION.equals(permission)) {
            if (currentUserID == null) {
                return false;
            }
            if (currentUserID.equals(targetDomainObject.getId())) {
                if (SecurityUtils.containsAuthority(authentication, UserWebsitePermissionConstant.USER_ALL_SELF_UPDATE)) {
                    return true;
                }
            } else {
                if (SecurityUtils.containsAuthority(authentication, UserWebsitePermissionConstant.USER_ALL_ALL_UPDATE)) {
                    return true;
                }
            }
            return false;
        }

        //Kiểm tra quyền read details.
        if (UserWebsiteActionPermissionRequest.READ_DETAIL_PERMISSION.equals(permission)) {
            //Everyone is allowed to read specific user details.
            return true;
        }

        throw new IllegalArgumentException("Permission not supported.");
    }
}
