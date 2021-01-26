package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Doc.DocDetailsDTO;
import com.bhtcnpm.website.model.dto.Doc.DocDetailsListDTO;
import com.bhtcnpm.website.model.dto.Doc.DocRequestDTO;
import com.querydsl.core.types.Predicate;

import javax.validation.constraints.Min;

public interface DocService {

    DocDetailsListDTO getAllDoc (Predicate predicate, @Min(0)Integer paginator);

    DocDetailsDTO putDoc (Long docID, Long lastEditedUserID, DocRequestDTO docRequestDTO);

    Boolean postApproval (Long docID, Long userID);

    Boolean deleteApproval (Long docID);
}
