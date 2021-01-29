package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Doc.DocDetailsDTO;
import com.bhtcnpm.website.model.dto.Doc.DocDetailsListDTO;
import com.bhtcnpm.website.model.dto.Doc.DocRequestDTO;
import com.querydsl.core.types.Predicate;

import javax.validation.constraints.Min;
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
}
