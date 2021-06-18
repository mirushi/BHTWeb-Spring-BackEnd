package com.bhtcnpm.website.security.evaluator.Doc;

import com.bhtcnpm.website.constant.domain.Doc.DocApprovalState;
import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.DocPermissionConstant;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.repository.DocRepository;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocPermissionEvaluator implements SimplePermissionEvaluator {

    private final DocRepository docRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        if (!(targetDomainObject instanceof Doc)) {
            throw new IllegalArgumentException(String.format("Target Domain Object type %s is not supported in DocPermissionEvaluator.",
                    targetDomainObject.getClass().getSimpleName().toUpperCase()));
        }

        return checkDocPermission(authentication, (Doc)targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String permission) {
        if (!(targetId instanceof Long)) {
            throw new IllegalArgumentException(String.format("Target ID %s is not Long. Denying access.", targetId.toString()));
        }

        Long targetIDLong = (Long) targetId;
        Optional<Doc> object = docRepository.findById(targetIDLong);

        //Target ID does not correspond to any object. Therefore we don't grant any permission.
        if (object.isEmpty()) {
            return false;
        }

        Doc entity = object.get();

        return checkDocPermission(authentication, entity, permission);
    }

    private boolean checkDocPermission(Authentication authentication, Doc targetDomainObject, String permission) {
        //Kiểm tra xem Doc hiện tại đang có state như thế nào.
        DocBusinessState state = targetDomainObject.getDocBusinessState();
        DocApprovalState approvalState = targetDomainObject.getDocApprovalState();

        UUID authenticatedUserID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        if (state == null) {
            this.log.warn("Doc business state cannot be determined. Denying access to object %s");
            return false;
        }

        //Kiểm tra quyền Read.
        if (DocActionPermissionRequest.READ_PERMISSION.equals(permission)) {
            return this.checkDocReadPermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        throw new IllegalArgumentException(String.format("Doc permission %s is not supported. Denying access to docID = %s", permission, targetDomainObject.getId()));
    }

    private boolean checkDocReadPermission (Authentication authentication, UUID authenticatedUserID, DocBusinessState state, Doc targetDomainObject) {
        //Check tương ứng với từng DocBusinessState.
        if (DocBusinessState.PUBLIC.equals(state)) {
            return true;
        }
        else if (DocBusinessState.UNLISTED.equals(state)) {
            //Tác giả của Doc có thể xem Doc của chính mình.
            if (authenticatedUserID == null) {
                log.warn("User ID not found in authentication object. Denying access.");
                return false;
            }
            if (targetDomainObject.getAuthor().getId().equals(authenticatedUserID)) {
                return true;
            }
            //Người có quyền xem bài viết UNLISTED có thể xem được bài viết này.
            if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_UNLISTED_ALL_READ)) {
                return true;
            }
            return false;
        }
        else if (DocBusinessState.DELETED.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_DELETED_ALL_READ)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
