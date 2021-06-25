package com.bhtcnpm.website.service.ExerciseComment;

import com.bhtcnpm.website.constant.business.ExerciseComment.ExerciseCommentActionAvailableConstant;
import com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseCommentActionPermissionRequest;
import com.bhtcnpm.website.model.dto.ExerciseComment.*;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseComment;
import com.bhtcnpm.website.model.entity.ExerciseEntities.UserExerciseCommentLike;
import com.bhtcnpm.website.model.entity.ExerciseEntities.UserExerciseCommentLikeId;
import com.bhtcnpm.website.repository.ExerciseComment.ExerciseCommentRepository;
import com.bhtcnpm.website.repository.ExerciseComment.UserExerciseCommentLikeRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.evaluator.ExerciseComment.ExerciseCommentPermissionEvaluator;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.util.PaginatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseCommentImpl implements ExerciseCommentService {
    private static final int PAGE_SIZE = 10;
    private static final int CHILD_PAGE_SIZE = 100;
    private final ExerciseCommentRepository exerciseCommentRepository;
    private final UserExerciseCommentLikeRepository userExerciseCommentLikeRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final ExerciseCommentMapper exerciseCommentMapper;
    private final ExerciseCommentPermissionEvaluator exerciseCommentPermissionEvaluator;

    @Override
    public ExerciseCommentListDTO getExerciseCommentsByExerciseID(Long exerciseID, Pageable pageable) {
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);
        Page<ExerciseCommentDTO> exerciseCommentDTOs = exerciseCommentRepository.getExerciseCommentDTOsParentOnly(exerciseID, pageable);
        return exerciseCommentMapper.exerciseCommentPageToExerciseCommentListDTO(exerciseCommentDTOs);
    }

    @Override
    public List<ExerciseCommentChildDTO> getChildComments(Long parentCommentID, Pageable pageable) {
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);
        List<ExerciseComment> queryResult = exerciseCommentRepository.getExerciseCommentByParentCommentId(parentCommentID, pageable);
        List<ExerciseCommentChildDTO> exerciseCommentDTOs = exerciseCommentMapper.exerciseCommentListToExerciseCommentChildDTOList(queryResult);
        return exerciseCommentDTOs;
    }

    @Override
    public ExerciseCommentDTO postExerciseComment(ExerciseCommentRequestDTO exerciseCommentRequestDTO, Long exerciseID, Authentication authentication) {
        UUID authorID = SecurityUtils.getUserID(authentication);
        if (authorID == null) {
            throw new IllegalArgumentException("Cannot extract userID from authentication.");
        }

        if (exerciseCommentRequestDTO == null) {
            return null;
        }

        ExerciseComment exerciseComment = exerciseCommentMapper.exerciseCommentDTOToExerciseComment(exerciseCommentRequestDTO, exerciseID, null, authorID, null);

        exerciseComment = exerciseCommentRepository.save(exerciseComment);

        return exerciseCommentMapper.exerciseCommentToExerciseCommentDTOChildCommentOnly(exerciseComment);
    }

    @Override
    public ExerciseCommentChildDTO postChildComment(ExerciseCommentRequestDTO exerciseCommentRequestDTO, Long parentCommentID, Authentication authentication) {
        UUID authorID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Optional<ExerciseComment> parentComment = exerciseCommentRepository.findById(parentCommentID);
        if (parentComment.isEmpty()) {
            throw new IllegalArgumentException("Parent comment not found.");
        }

        ExerciseComment parentEntity = parentComment.get();
        //We don't want it to be nested too deep.
        if (parentEntity.getParentComment() != null) {
            throw new IllegalArgumentException("Cannot nest comment. Maximum comment depth is 2.");
        }

        ExerciseComment childEntity = exerciseCommentMapper.exerciseCommentDTOToExerciseComment(exerciseCommentRequestDTO, parentEntity.getExercise().getId(), parentCommentID, authorID, null);
        exerciseCommentRepository.save(childEntity);

        return exerciseCommentMapper.exerciseCommentToExerciseCommentChildDTO(childEntity);
    }

    @Override
    public ExerciseCommentDTO putExerciseComment(ExerciseCommentRequestDTO exerciseCommentRequestDTO, Long commentID, Authentication authentication) {
        if (exerciseCommentRequestDTO == null) {
            return null;
        }

        Optional<ExerciseComment> optionalExerciseComment = exerciseCommentRepository.findById(commentID);
        if (!(optionalExerciseComment.isPresent())) {
            return null;
        }

        ExerciseComment exerciseComment = optionalExerciseComment.get();

        Long parentCommentId = null;
        if (exerciseComment.getParentComment() != null) {
            parentCommentId = exerciseComment.getParentComment().getId();
        }

        exerciseComment = exerciseCommentMapper.exerciseCommentDTOToExerciseComment(
                exerciseCommentRequestDTO, exerciseComment.getExercise().getId(), parentCommentId, exerciseComment.getAuthor().getId(), exerciseComment
        );

        exerciseComment = exerciseCommentRepository.save(exerciseComment);

        return exerciseCommentMapper.exerciseCommentToExerciseCommentDTOChildCommentOnly(exerciseComment);
    }

    @Override
    public boolean deleteExerciseComment(Long commentID) {
        exerciseCommentRepository.deleteById(commentID);
        return true;
    }

    @Override
    public boolean createUserExerciseCommentLike(Long commentID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        UserExerciseCommentLikeId id =
                new UserExerciseCommentLikeId(userWebsiteRepository.getOne(userID), exerciseCommentRepository.getOne(commentID));
        UserExerciseCommentLike entity = new UserExerciseCommentLike(id);
        userExerciseCommentLikeRepository.save(entity);
        return true;
    }

    @Override
    public boolean deleteUserExerciseCommentLike(Long commentID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        UserExerciseCommentLikeId id =
                new UserExerciseCommentLikeId(userWebsiteRepository.getOne(userID), exerciseCommentRepository.getOne(commentID));
        userExerciseCommentLikeRepository.deleteById(id);
        return true;
    }

    @Override
    public List<ExerciseCommentStatisticDTO> getCommentStatistics(List<Long> commentIDs, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);

        List<ExerciseCommentStatisticDTO> exerciseCommentStatisticDTOs = exerciseCommentRepository.getExerciseCommentStatisticDTOs(commentIDs, userID);

        return exerciseCommentStatisticDTOs;
    }

    @Override
    public List<ExerciseCommentAvailableActionDTO> getAvailableExerciseCommentAction(List<Long> exerciseCommentIDs, Authentication authentication) {
        List<ExerciseCommentAvailableActionDTO> exerciseCommentAvailableActionDTOList = new ArrayList<>();

        for (Long exerciseCommentID : exerciseCommentIDs) {
            if (exerciseCommentID == null) {
                continue;
            }
            ExerciseCommentAvailableActionDTO exerciseCommentAvailableActionDTO = new ExerciseCommentAvailableActionDTO();
            exerciseCommentAvailableActionDTO.setId(exerciseCommentID);
            List<String> availableActionList = new ArrayList<>();

            if (exerciseCommentPermissionEvaluator.hasPermission(authentication, exerciseCommentID, ExerciseCommentActionPermissionRequest.READ_PERMISSION)) {
                availableActionList.add(ExerciseCommentActionAvailableConstant.READ_ACTION);
            }
            if (exerciseCommentPermissionEvaluator.hasPermission(authentication, exerciseCommentID, ExerciseCommentActionPermissionRequest.UPDATE_PERMISSION)) {
                availableActionList.add(ExerciseCommentActionAvailableConstant.UPDATE_ACTION);
            }
            if (exerciseCommentPermissionEvaluator.hasPermission(authentication, exerciseCommentID, ExerciseCommentActionPermissionRequest.DELETE_PERMISSION)) {
                availableActionList.add(ExerciseCommentActionAvailableConstant.DELETE_ACTION);
            }
            if (exerciseCommentPermissionEvaluator.hasPermission(authentication, exerciseCommentID, ExerciseCommentActionPermissionRequest.LIKE_PERMISSION)) {
                availableActionList.add(ExerciseCommentActionAvailableConstant.LIKE_ACTION);
            }
            if (exerciseCommentPermissionEvaluator.hasPermission(authentication, exerciseCommentID, ExerciseCommentActionPermissionRequest.REPLY_PERMISSION)) {
                availableActionList.add(ExerciseCommentActionAvailableConstant.REPLY_ACTION);
            }
            if (exerciseCommentPermissionEvaluator.hasPermission(authentication, exerciseCommentID, ExerciseCommentActionPermissionRequest.REPORT_PERMISSION)) {
                availableActionList.add(ExerciseCommentActionAvailableConstant.REPORT_ACTION);
            }

            exerciseCommentAvailableActionDTO.setAvailableActions(availableActionList);
            exerciseCommentAvailableActionDTOList.add(exerciseCommentAvailableActionDTO);
        }

        return exerciseCommentAvailableActionDTOList;
    }
}
