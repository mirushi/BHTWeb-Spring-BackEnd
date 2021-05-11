package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.DocComment.DocCommentDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentListDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentMapper;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentRequestDTO;
import com.bhtcnpm.website.model.entity.DocEntities.DocComment;
import com.bhtcnpm.website.repository.DocCommentRepository;
import com.bhtcnpm.website.service.DocCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocCommentServiceImpl implements DocCommentService {

    private static final Integer PAGE_SIZE = 10;

    private final DocCommentRepository docCommentRepository;

    private final DocCommentMapper docCommentMapper;

    @Override
    public DocCommentListDTO getDocComment (Integer pageNum, Long docID) {
        Pageable pageable = PageRequest.of(pageNum,PAGE_SIZE);

        Page<DocComment> queryResults = docCommentRepository.findAllByDocIdAndParentCommentIsNull(pageable, docID);

        DocCommentListDTO result = docCommentMapper.docCommentPageToDocCommentListDTO(queryResults);

        return result;
    }

    @Override
    public Boolean postDocComment (DocCommentRequestDTO requestDTO, Long authorID, Long docID) {
        DocComment docComment = docCommentMapper.docCommentDTOToDocComment(requestDTO, authorID, docID, null);

        docCommentRepository.save(docComment);
        return true;
    }

    @Override
    public DocCommentDTO putDocComment(DocCommentRequestDTO requestDTO, Long commentID, Long userID) {
        //TODO: Please check if user has sufficient right to change comment.
        Optional<DocComment> optionalDocComment = docCommentRepository.findById(commentID);

        if (!optionalDocComment.isPresent()) {
            return null;
        }

        DocComment docComment = optionalDocComment.get();

        docComment = docCommentMapper.docCommentDTOToDocComment(
                requestDTO, docComment.getAuthor().getId(), docComment.getDoc().getId(), docComment);

        docComment = docCommentRepository.save(docComment);

        return docCommentMapper.docCommentToDocCommentDTO(docComment);
    }

    @Override
    public Boolean deleteDocComment(Long commentID, Long userID) {
        //TODO: Please check if user has enough permission to perform delete comment.
        DocComment comment = docCommentRepository.getOne(commentID);
        docCommentRepository.delete(comment);

        return true;
    }
}
