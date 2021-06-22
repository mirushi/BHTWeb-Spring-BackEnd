package com.bhtcnpm.website.service.Doc;

import com.bhtcnpm.website.model.dto.DocComment.DocCommentDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentListDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentRequestDTO;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DocCommentService {
    DocCommentListDTO getDocComment (Integer pageNum, Long docID);

    Boolean postDocComment (DocCommentRequestDTO requestDTO, UUID authorID, Long docID);

    DocCommentDTO putDocComment(DocCommentRequestDTO requestDTO, Long commentID, UUID userID);

    Boolean deleteDocComment (Long commentID, UUID userID);
}
