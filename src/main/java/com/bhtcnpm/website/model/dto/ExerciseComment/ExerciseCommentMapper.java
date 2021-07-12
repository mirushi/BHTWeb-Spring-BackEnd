package com.bhtcnpm.website.model.dto.ExerciseComment;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseComment;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.ExerciseComment.ExerciseCommentRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mapper
public abstract class ExerciseCommentMapper {
    public static final ExerciseCommentMapper INSTANCE = Mappers.getMapper(ExerciseCommentMapper.class);

    protected ExerciseCommentRepository exerciseCommentRepository;
    protected ExerciseRepository exerciseRepository;
    protected UserWebsiteRepository userWebsiteRepository;

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    @Mapping(target = "childCommentCount", ignore = true)
    public abstract ExerciseCommentDTO exerciseCommentToExerciseCommentDTOChildCommentOnly (ExerciseComment exerciseComment);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    public abstract ExerciseCommentChildDTO exerciseCommentToExerciseCommentChildDTO (ExerciseComment exerciseComment);

    public ExerciseCommentListDTO exerciseCommentPageToExerciseCommentListDTO (Page<ExerciseCommentDTO> exerciseCommentDTOs) {
        return ExerciseCommentListDTO.builder()
                .exerciseCommentDTOs(exerciseCommentDTOs.getContent())
                .totalPages(exerciseCommentDTOs.getTotalPages())
                .totalElements(exerciseCommentDTOs.getTotalElements())
                .build();
    }

    public ExerciseComment exerciseCommentDTOToExerciseComment (ExerciseCommentRequestDTO exerciseCommentRequestDTO,
                                                                Long exerciseID, Long parentCommentID, UUID authorID, ExerciseComment entity) {
        ExerciseComment exerciseComment = Objects.requireNonNullElseGet(entity, ExerciseComment::new);

        if (exerciseID == null || exerciseCommentRequestDTO == null || authorID == null) {
            throw new IllegalArgumentException("ExerciseID, ExerciseCommentRequestDTO and authorID cannot be null.");
        }

        if (parentCommentID != null) {
            exerciseComment.setParentComment(exerciseCommentRepository.getOne(parentCommentID));
        } else {
            exerciseComment.setParentComment(null);
        }

        exerciseComment.setExercise(exerciseRepository.getOne(exerciseID));
        exerciseComment.setAuthor(userWebsiteRepository.getOne(authorID));
        exerciseComment.setContent(exerciseCommentRequestDTO.getContent());

        //TODO: Do XSS filter here.

        return exerciseComment;
    }

    public abstract List<ExerciseCommentChildDTO> exerciseCommentListToExerciseCommentChildDTOList (List<ExerciseComment> exerciseComments);

    public abstract List<ExerciseCommentDTO> exerciseCommentListToExerciseCommentDTOListChildCommentOnly (List<ExerciseComment> exerciseComments);

    @Autowired
    public void setExerciseCommentRepository (ExerciseCommentRepository exerciseCommentRepository) {
        this.exerciseCommentRepository = exerciseCommentRepository;
    }

    @Autowired
    public void setExerciseRepository (ExerciseRepository exerciseRepository) {this.exerciseRepository = exerciseRepository;}

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) {
        this.userWebsiteRepository = userWebsiteRepository;
    }
}
