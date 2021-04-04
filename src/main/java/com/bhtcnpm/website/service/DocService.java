package com.bhtcnpm.website.service;

import com.bhtcnpm.website.constant.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.google.api.services.drive.model.File;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.List;

public interface DocService {

    DocDetailsListDTO getAllDoc (Predicate predicate, @Min(0)Integer paginator);

    DocDetailsDTO putDoc (Long docID, Long lastEditedUserID, DocRequestDTO docRequestDTO);

    Boolean postApproval (Long docID, Long userID);

    Boolean deleteApproval (Long docID);

    Boolean increaseDownloadCount (Long docID, Long userID);

    Boolean postReject(Long docID, Long userID);

    Boolean undoReject(Long docID, Long userID);

    DocDetailsDTO createDocument (DocRequestDTO docRequestDTO);

    List<DocDetailsDTO> getRelatedDocs (Long docID);

    List<DocSummaryDTO> getTrending ();

    public List<DocStatisticDTO> getDocStatistics(List<Long> docIDs, Long userID);

    DocDetailsDTO createDoc (DocRequestDTO docRequestDTO, Long userID);

    DocSummaryListDTO getDocBySearchTerm(
            String searchTerm,
            Integer page,
            ApiSortOrder sortByPublishDtm,
            Long categoryID,
            Long subjectID
    );
    DocUploadDTO uploadFileToGDrive(MultipartFile multipartFile, Long userID) throws IOException, FileExtensionNotAllowedException;

    DocDownloadInfoDTO getDocDownloadInfo (String fileCode);

    DocSummaryWithStateListDTO getManagementDoc(
            String searchTerm,
            DocStateType docStateType,
            Long subjectID,
            Long categoryID,
            Integer page
    );
}
