package com.bhtcnpm.website.service.Doc;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.model.validator.dto.Doc.DocActionRequestSize;
import com.bhtcnpm.website.model.validator.dto.Doc.DocID;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DocService {

    @PreAuthorize(value = "permitAll()")
    DocSummaryListDTO getAllDoc (Predicate predicate, Pageable pageable,boolean mostLiked, boolean mostViewed, boolean mostDownloaded, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.DocPermissionConstant).DOC_UNLISTED_ALL_READ)")
    DocDetailsWithStateListDTO getAllPendingApprovalDoc (
            Predicate predicate,
            Pageable pageable,
            Authentication authentication
    );

    @PreAuthorize(value = "isAuthenticated()")
    DocSummaryWithStateAndFeedbackListDTO getMyDocuments (String searchTerm,
                                               Long categoryID,
                                               Long subjectID,
                                               DocStateType docState,
                                               Integer page,
                                               ApiSortOrder sortByPublishDtm,
                                               ApiSortOrder sortByCreatedDtm,
                                               Authentication authentication);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).UPDATE_PERMISSION)")
    DocDetailsDTO putDoc (Long docID, DocRequestDTO docRequestDTO, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).APPROVE_PERMISSION)")
    Boolean postApproval (Long docID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).APPROVE_PERMISSION)")
    Boolean deleteApproval (Long docID);

    Boolean increaseDownloadCount (Long docID, Long userID);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).APPROVE_PERMISSION)")
    Boolean docReject(Long docID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).APPROVE_PERMISSION)")
    Boolean rejectDocWithFeedback (Long docID, String feedback);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).APPROVE_PERMISSION)")
    Boolean undoReject(Long docID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).SAVE_PERMISSION)")
    Boolean createSavedStatus (@DocID Long docID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).SAVE_PERMISSION)")
    Boolean deleteSavedStatus (Long docID, Authentication authentication);

    @PreAuthorize(value = "isAuthenticated()")
    DocSummaryListDTO getDocSavedByUserOwn (Predicate predicate, Authentication authentication, Pageable pageable);

    @PreAuthorize(value = "permitAll()")
    List<DocSuggestionDTO> getRelatedDocs (Long postID, Long docID, Long exerciseID,
                                           UUID authorID, Long categoryID, Long subjectID,
                                           Integer page, Authentication authentication) throws IOException;

    @PreAuthorize(value = "permitAll()")
    List<DocSummaryDTO> getHotDocs(Pageable pageable, Authentication authentication);

    @PreFilter(filterTarget = "docIDs", value = "hasPermission(filterObject, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).READ_PERMISSION)")
    List<DocStatisticDTO> getDocStatistics(List<Long> docIDs, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.DocPermissionConstant).DOC_PENDING_SELF_CREATE)")
    DocDetailsDTO createDoc (DocRequestDTO docRequestDTO, Authentication authentication);

    //TODO: Implement permission here.
    @PreAuthorize(value = "isAuthenticated()")
    String uploadImage (MultipartFile multipartFile, Authentication authentication) throws FileExtensionNotAllowedException, IOException;

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).DELETE_PERMISSION)")
    Boolean deleteDoc (@DocID Long docID, Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    DocSummaryListDTO getDocBySearchTerm(
            String searchTerm,
            Long categoryID,
            Long subjectID,
            UUID authorID,
            Long tagID,
            Integer page,
            ApiSortOrder sortByPublishDtm,
            Authentication authentication
    );

    @PreAuthorize(value = "hasPermission(#id, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).READ_PERMISSION)")
    DocDetailsDTO getDocDetails (Long id);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.DocPermissionConstant).DOC_PENDING_SELF_UPLOAD)")
    DocFileUploadDTO uploadFileToGDrive(MultipartFile multipartFile, Authentication authentication) throws IOException, FileExtensionNotAllowedException;

    DocDownloadInfoDTO getDocDownloadInfo (UUID fileID);

    @PreAuthorize(value = "permitAll()")
    List<DocAvailableActionDTO> getAvailableDocAction (@DocActionRequestSize List<Long> docIDs, Authentication authentication);

    @PreAuthorize(value = "isAuthenticated()")
    DocSummaryWithStateListDTO getManagementDoc(
            String searchTerm,
            Long categoryID,
            Long subjectID,
            UUID authorID,
            DocStateType docState,
            Integer page,
            ApiSortOrder sortByPublishDtm,
            ApiSortOrder sortByCreatedDtm,
            Authentication authentication
    );

    List<DocQuickSearchResult> quickSearch (Pageable pageable, String searchTerm);
}
