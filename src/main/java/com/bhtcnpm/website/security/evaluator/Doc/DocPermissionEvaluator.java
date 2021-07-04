package com.bhtcnpm.website.security.evaluator.Doc;

import com.bhtcnpm.website.constant.domain.Doc.DocApprovalState;
import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.constant.security.evaluator.GenericOwnership;
import com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest;
import com.bhtcnpm.website.constant.security.permission.DocPermissionConstant;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.repository.Doc.DocRepository;
import com.bhtcnpm.website.security.evaluator.base.SimplePermissionEvaluator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        UUID authenticatedUserID = SecurityUtils.getUserID(authentication);

        if (state == null) {
            this.log.warn("Doc business state cannot be determined. Denying access to object %s");
            return false;
        }

        //Kiểm tra quyền Read.
        if (DocActionPermissionRequest.READ_PERMISSION.equals(permission)) {
            return this.checkDocReadPermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Update.
        if (DocActionPermissionRequest.UPDATE_PERMISSION.equals(permission)) {
            return this.checkDocUpdatePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Approve.
        if (DocActionPermissionRequest.APPROVE_PERMISSION.equals(permission)) {
            return this.checkDocApprovePermission(authentication, authenticatedUserID, approvalState);
        }

        //Kiểm tra quyền Save.
        if (DocActionPermissionRequest.SAVE_PERMISSION.equals(permission)) {
            return this.checkDocSavePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        //Kiểm tra quyền Delete.
        if (DocActionPermissionRequest.DELETE_PERMISSION.equals(permission)) {
            return this.checkDocDeletePermission(authentication, authenticatedUserID, state, targetDomainObject);
        }

        throw new IllegalArgumentException(String.format("Doc permission %s is not supported. Denying access to docID = %s", permission, targetDomainObject.getId()));
    }

    private boolean checkDocDeletePermission (Authentication authentication, UUID authenticatedUserID, DocBusinessState state, Doc targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được deleted.
        log.info("Checking doc delete permission.");
        if (authenticatedUserID == null) {
            log.warn("User ID not found in authentication object. Denying access.");
            return false;
        }

        //Xét state của Doc.
        if (DocBusinessState.PUBLIC.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject, DocPermissionConstant.DOC_PUBLIC_SELF_DELETE)) {
                return true;
            } else if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_PUBLIC_ALL_DELETE)) {
                return true;
            }
            return false;
        }
        else if (DocBusinessState.UNLISTED.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject, DocPermissionConstant.DOC_UNLISTED_SELF_DELETE)) {
                return true;
            }
            else if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_UNLISTED_ALL_DELETE)) {
                return true;
            }
            return false;
        }
        else if (DocBusinessState.DELETED.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_DELETED_ALL_DELETE)) {
                return true;
            }
            return false;
        }

        return false;
    }

    private boolean checkDocSavePermission (Authentication authentication, UUID authenticatedUserID, DocBusinessState state, Doc targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được save.
        log.info("Checking doc save permission.");
        if (authenticatedUserID == null) {
            log.warn("User ID not found in authentication object. Denying access.");
            return false;
        }

        //Xét state của Doc.
        if (DocBusinessState.PUBLIC.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_PUBLIC_ALL_SAVE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkDocApprovePermission (Authentication authentication, UUID authenticatedUserID, DocApprovalState approvalState) {
        //Bắt buộc phải có tài khoản mới được approve.
        log.info("Checking doc approve permission.");
        if (authenticatedUserID == null) {
            log.warn("User ID not found in authentication object. Denying access.");
            return false;
        }

        //Xét state duyệt của Doc.
        //Nếu Doc đang pending thì xét xem User có quyền để thực hiện Approve/Reject hay không.
        if (DocApprovalState.PENDING.equals(approvalState)) {
            if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_PENDING_ALL_APPROVE)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkDocUpdatePermission (Authentication authentication, UUID authenticatedUserID, DocBusinessState state, Doc targetDomainObject) {
        //Bắt buộc phải có tài khoản mới được update.
        log.info("Checking post update permission.");
        if (authenticatedUserID == null) {
            log.warn("User ID not found in authentication object. Denying access.");
            return false;
        }
        //Xét state của Doc.
        if (DocBusinessState.PUBLIC.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject, DocPermissionConstant.DOC_PUBLIC_SELF_UPDATE)) {
                return true;
            }
            else if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_PUBLIC_ALL_UPDATE)) {
                return true;
            }
            return false;
        }
        else if (DocBusinessState.UNLISTED.equals(state)) {
            if (isOwnerAndContainsAuthority(authentication, targetDomainObject, DocPermissionConstant.DOC_UNLISTED_SELF_UPDATE)) {
                return true;
            }
            else if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_UNLISTED_ALL_UPDATE)) {
                return true;
            }
            return false;
        }
        else if (DocBusinessState.DELETED.equals(state)) {
            if (SecurityUtils.containsAuthority(authentication, DocPermissionConstant.DOC_DELETED_ALL_UPDATE)) {
                return true;
            }
            return false;
        }
        return false;
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

    private boolean isOwnerAndContainsAuthority (Authentication authentication, Doc targetDomainObject, String permission) {
        UUID authenticatedUserID = SecurityUtils.getUserIDOnNullThrowException(authentication);
        if (GenericOwnership.OWNER.equals(getOwnership(authenticatedUserID, targetDomainObject))
                && SecurityUtils.containsAuthority(authentication, permission)) {
            return true;
        }
        return false;
    }

    private GenericOwnership getOwnership (UUID userID, Doc doc) {
        if (doc.getAuthor().getId().equals(userID)) {
            return GenericOwnership.OWNER;
        }
        return GenericOwnership.NONE;
    }

}
