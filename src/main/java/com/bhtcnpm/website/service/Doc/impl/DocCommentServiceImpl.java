package com.bhtcnpm.website.service.Doc.impl;

import com.bhtcnpm.website.constant.business.DocComment.DocCommentActionAvailableConstant;
import com.bhtcnpm.website.constant.security.evaluator.permission.DocCommentActionPermissionRequest;
import com.bhtcnpm.website.model.dto.DocComment.*;
import com.bhtcnpm.website.model.entity.DocCommentEntities.DocComment;
import com.bhtcnpm.website.model.entity.DocCommentEntities.UserDocCommentLike;
import com.bhtcnpm.website.model.entity.DocCommentEntities.UserDocCommentLikeId;
import com.bhtcnpm.website.repository.Doc.DocCommentRepository;
import com.bhtcnpm.website.repository.Doc.UserDocCommentLikeRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.evaluator.DocComment.DocCommentPermissionEvaluator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Doc.DocCommentService;
import com.bhtcnpm.website.service.util.PaginatorUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DocCommentServiceImpl implements DocCommentService {

    private static final int PAGE_SIZE = 10;

    private static final int CHILD_PAGE_SIZE = 100;

    private final DocCommentRepository docCommentRepository;

    private final UserDocCommentLikeRepository userDocCommentLikeRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final DocCommentMapper docCommentMapper;

    private final DocCommentPermissionEvaluator docCommentPermissionEvaluator;

    @Override
    public DocCommentListDTO getDocCommentsByDocID(Long docID, Pageable pageable) {
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);

        Page<DocCommentDTO> docCommentDTOs = docCommentRepository.getDocCommentDTOsParentOnly(docID, pageable);

        return docCommentMapper.docCommentPageToDocCommentListDTO(docCommentDTOs);
    }

    @Override
    public List<DocCommentChildDTO> getChildComments(Long parentCommentID, Pageable pageable) {
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, CHILD_PAGE_SIZE);

        List<DocComment> queryResult = docCommentRepository.getDocCommentByParentCommentId(parentCommentID, pageable);

        List<DocCommentChildDTO> docCommentDTOs = docCommentMapper.docCommentListToDocCommentChildDTOList(queryResult);

        return docCommentDTOs;
    }

    @Override
    public DocCommentDTO postDocComment(DocCommentRequestDTO docCommentRequestDTO, Long docID, Authentication authentication) {
        UUID authorID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Validate.notNull(docCommentRequestDTO, "Doc request body cannot be null.");

        DocComment docComment = docCommentMapper.docCommentDTOToDocComment(docCommentRequestDTO, docID, null, authorID, null);

        docComment = docCommentRepository.save(docComment);

        return docCommentMapper.docCommentToDocCommentDTO(docComment);
    }

    @Override
    public DocCommentChildDTO postChildComment(DocCommentRequestDTO docCommentRequestDTO, Long parentCommentID, Authentication authentication) {
        UUID authorID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Optional<DocComment> parentComment = docCommentRepository.findById(parentCommentID);
        Validate.isTrue(parentComment.isPresent(), "Parent comment not found.");

        DocComment parentEntity = parentComment.get();
        //We don't want it to be nested too deep.
        if (parentEntity.getParentComment() != null) {
            throw new IllegalArgumentException("Cannot nest comment. Maximum content depth is 2.");
        }

        DocComment childEntity = docCommentMapper.docCommentDTOToDocComment(docCommentRequestDTO, parentEntity.getDoc().getId(), parentCommentID, authorID, null);
        childEntity = docCommentRepository.save(childEntity);

        return docCommentMapper.docCommentToDocCommentChildDTO(childEntity);
    }

    @Override
    public DocCommentDTO putDocComment(DocCommentRequestDTO docCommentRequestDTO, Long commentID, Authentication authentication) {
        Validate.notNull(docCommentRequestDTO, "Doc request body cannot be null.");

        Optional<DocComment> optionalDocComment = docCommentRepository.findById(commentID);
        Validate.isTrue(optionalDocComment.isPresent(), "Comment not found.");

        DocComment docComment = optionalDocComment.get();

        Long parentCommentID = null;
        if (docComment.getParentComment() != null) {
            parentCommentID = docComment.getParentComment().getId();
        }

        docComment = docCommentMapper.docCommentDTOToDocComment(docCommentRequestDTO,
                docComment.getDoc().getId(), parentCommentID, docComment.getAuthor().getId(), docComment);

        docComment = docCommentRepository.save(docComment);

        return docCommentMapper.docCommentToDocCommentDTO(docComment);
    }

    @Override
    public boolean deleteDocComment(Long commentID) {
        docCommentRepository.deleteById(commentID);
        return true;
    }

    @Override
    public boolean createUserDocCommentLike(Long commentID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        UserDocCommentLikeId id =
                new UserDocCommentLikeId(userWebsiteRepository.getOne(userID), docCommentRepository.getOne(commentID));
        UserDocCommentLike entity = new UserDocCommentLike(id);
        userDocCommentLikeRepository.save(entity);
        return true;
    }

    @Override
    public boolean deleteUserDocCommentLike(Long commentID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        UserDocCommentLikeId id =
                new UserDocCommentLikeId(userWebsiteRepository.getOne(userID), docCommentRepository.getOne(commentID));
        userDocCommentLikeRepository.deleteById(id);
        return true;
    }

    @Override
    public List<DocCommentStatisticDTO> getCommentStatistics(List<Long> commentIDs, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);

        List<DocCommentStatisticDTO> docCommentStatisticDTOs = docCommentRepository.getDocCommentStatisticDTOs(commentIDs, userID);

        return docCommentStatisticDTOs;
    }

    @Override
    public List<DocCommentAvailableActionDTO> getAvailableDocCommentAction(List<Long> docCommentIDs, Authentication authentication) {
        List<DocCommentAvailableActionDTO> docCommentAvailableActionDTOList = new ArrayList<>();

        for (Long docCommentID : docCommentIDs) {
            if (docCommentID == null) {
                continue;
            }
            List<String> availableActionList = new ArrayList<>();

            if (docCommentPermissionEvaluator.hasPermission(authentication, docCommentID, DocCommentActionPermissionRequest.READ_PERMISSION)) {
                availableActionList.add(DocCommentActionAvailableConstant.READ_ACTION);
            }
            if (docCommentPermissionEvaluator.hasPermission(authentication, docCommentID, DocCommentActionPermissionRequest.UPDATE_PERMISSION)) {
                availableActionList.add(DocCommentActionAvailableConstant.UPDATE_ACTION);
            }
            if (docCommentPermissionEvaluator.hasPermission(authentication, docCommentID, DocCommentActionPermissionRequest.DELETE_PERMISSION)) {
                availableActionList.add(DocCommentActionAvailableConstant.DELETE_ACTION);
            }
            if (docCommentPermissionEvaluator.hasPermission(authentication, docCommentID, DocCommentActionPermissionRequest.LIKE_PERMISSION)) {
                availableActionList.add(DocCommentActionAvailableConstant.LIKE_ACTION);
            }
            if (docCommentPermissionEvaluator.hasPermission(authentication, docCommentID, DocCommentActionPermissionRequest.REPLY_PERMISSION)) {
                availableActionList.add(DocCommentActionAvailableConstant.REPLY_ACTION);
            }
            if (docCommentPermissionEvaluator.hasPermission(authentication, docCommentID, DocCommentActionPermissionRequest.REPORT_PERMISSION)) {
                availableActionList.add(DocCommentActionAvailableConstant.REPORT_ACTION);
            }

            DocCommentAvailableActionDTO docCommentAvailableActionDTO = DocCommentAvailableActionDTO.builder()
                    .id(docCommentID)
                    .availableActions(availableActionList)
                    .build();
            docCommentAvailableActionDTOList.add(docCommentAvailableActionDTO);
        }

        return docCommentAvailableActionDTOList;
    }
}
