package com.bhtcnpm.website.service;

import com.bhtcnpm.website.constant.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DocService {

    @PreAuthorize(value = "permitAll()")
    DocSummaryListDTO getAllDoc (Predicate predicate, Pageable pageable, Authentication authentication);

    DocSummaryListDTO getAllPendingApprovalDoc (
            String searchTerm,
            Long subjectID,
            Long categoryID,
            Long authorID,
            Integer page,
            ApiSortOrder sortByCreatedDtm
    );

    DocSummaryWithStateListDTO getMyDocuments (String searchTerm,
                                      Long categoryID,
                                      Long subjectID,
                                      DocStateType docState,
                                      Integer page,
                                      ApiSortOrder sortByPublishDtm,
                                      ApiSortOrder sortByCreatedDtm,
                                      Long userID);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).UPDATE_PERMISSION)")
    DocDetailsDTO putDoc (Long docID, DocRequestDTO docRequestDTO, Authentication authentication);

    Boolean postApproval (Long docID, Long userID);

    Boolean deleteApproval (Long docID);

    Boolean increaseDownloadCount (Long docID, Long userID);

    Boolean postReject(Long docID, Long userID);

    Boolean undoReject(Long docID, Long userID);

    DocDetailsDTO createDocument (DocRequestDTO docRequestDTO);

    List<DocDetailsDTO> getRelatedDocs (Long docID);

    List<DocSuggestionDTO> getRelatedDocs (Long exerciseID, Integer page);

    List<DocSummaryDTO> getTrending ();

    public List<DocStatisticDTO> getDocStatistics(List<Long> docIDs, UUID userID);

    DocDetailsDTO createDoc (DocRequestDTO docRequestDTO, Authentication authentication);

    DocSummaryListDTO getDocBySearchTerm(
            String searchTerm,
            Long categoryID,
            Long subjectID,
            Long authorID,
            Integer page,
            ApiSortOrder sortByPublishDtm
    );

    DocUploadDTO uploadFileToGDrive(MultipartFile multipartFile, UUID userID) throws IOException, FileExtensionNotAllowedException;

    DocDownloadInfoDTO getDocDownloadInfo (String fileCode);

    DocSummaryWithStateListDTO getManagementDoc(
            String searchTerm,
            Long categoryID,
            Long subjectID,
            Long authorID,
            DocStateType docState,
            Integer page,
            ApiSortOrder sortByPublishDtm,
            ApiSortOrder sortByCreatedDtm
    );
}
