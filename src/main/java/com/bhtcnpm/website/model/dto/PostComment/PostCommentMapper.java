package com.bhtcnpm.website.model.dto.PostComment;

import com.bhtcnpm.website.model.entity.PostCommentEntities.PostComment;
import com.bhtcnpm.website.repository.Post.PostCommentRepository;
import com.bhtcnpm.website.repository.Post.PostRepository;
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
public abstract class PostCommentMapper {
    public static final PostCommentMapper INSTANCE = Mappers.getMapper(PostCommentMapper.class);

    protected PostCommentRepository postCommentRepository;
    protected PostRepository postRepository;
    protected UserWebsiteRepository userWebsiteRepository;

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    @Mapping(target = "childCommentCount", ignore = true)
    public abstract PostCommentDTO postCommentToPostCommentDTOChildCommentOnly (PostComment postComment);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    public abstract PostCommentChildDTO postCommentToPostCommentChildDTO (PostComment postComment);

    public PostCommentListDTO postCommentPageToPostCommentListDTO (Page<PostCommentDTO> postCommentDTOS) {
        return PostCommentListDTO.builder()
                .postCommentDTOs(postCommentDTOS.getContent())
                .totalPages(postCommentDTOS.getTotalPages())
                .totalElements(postCommentDTOS.getTotalElements())
                .build();
    }

    public PostComment postCommentDTOToPostComment(PostCommentRequestDTO postCommentRequestDTO,
                                                   Long postID, Long parentCommentID, UUID authorID, PostComment entity) {

        PostComment postComment = Objects.requireNonNullElseGet(entity, PostComment::new);

        if (postID == null || postCommentRequestDTO == null || authorID == null) {
            throw new IllegalArgumentException("PostID, PostCommentRequestDTO and authorID cannot be null.");
        }

        if (parentCommentID != null) {
            postComment.setParentComment(postCommentRepository.getOne(parentCommentID));
        } else {
            postComment.setParentComment(null);
        }

        postComment.setPost(postRepository.getOne(postID));
        postComment.setAuthor(userWebsiteRepository.getOne(authorID));
        postComment.setContent(postCommentRequestDTO.getContent());
        //TODO: Do XSS filter here.

        return postComment;
    }

    public abstract List<PostCommentChildDTO> postCommentListToPostCommentChildDTOList (List<PostComment> postComments);

    public abstract List<PostCommentDTO> postCommentListToPostCommentDTOListChildCommentOnly (List<PostComment> postComments);

    @Autowired
    public void setPostCommentRepository (PostCommentRepository postCommentRepository) {
        this.postCommentRepository = postCommentRepository;
    }

    @Autowired
    public void setPostRepository (PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) {
        this.userWebsiteRepository = userWebsiteRepository;
    }
}
