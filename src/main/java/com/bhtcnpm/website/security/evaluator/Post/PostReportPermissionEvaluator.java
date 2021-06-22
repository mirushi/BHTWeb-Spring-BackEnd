package com.bhtcnpm.website.security.evaluator.Post;

import com.bhtcnpm.website.model.entity.PostEntities.PostReport;
import com.bhtcnpm.website.repository.PostReportRepository;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostReportPermissionEvaluator implements SimplePermissionEvaluator {

    private final PostReportRepository postReportRepository;

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        if (!(targetDomainObject instanceof PostReport)) {
            logger.warn(LogMessage.format("Target Domain Object type %s is not supported in PostPermissionEvaluator. Denying permission.",
                    targetDomainObject.getClass().getSimpleName().toUpperCase()));
            return false;
        }
        return checkPostReportPermission(authentication, (PostReport) targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        if (!(targetId instanceof Long)) {
            logger.warn(LogMessage.format("Target ID %s is not Long. Denying access.", targetId.toString()));
            return false;
        }

        Long targetIDLong = (Long)targetId;
        Optional<PostReport> object = postReportRepository.findById(targetIDLong);

        //Target ID does not correspond to any object. Therefore we don't grant any permission.
        if (object.isEmpty()) {
            return false;
        }

        PostReport entity = object.get();

        return checkPostReportPermission(authentication, entity, permission);
    }

    private boolean checkPostReportPermission (Authentication authentication, PostReport postReport, String permission) {
        throw new NotImplementedException();
    }

}
