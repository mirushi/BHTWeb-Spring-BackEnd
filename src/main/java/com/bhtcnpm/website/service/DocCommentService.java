package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.DocComment.DocCommentDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentListDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentRequestDTO;
import org.springframework.data.domain.Pageable;

public interface DocCommentService {
    DocCommentListDTO getDocComment (Integer pageNum, Long docID);

    Boolean postDocComment (DocCommentRequestDTO requestDTO, Long authorID, Long docID);
}
