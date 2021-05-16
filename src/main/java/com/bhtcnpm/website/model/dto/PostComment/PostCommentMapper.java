package com.bhtcnpm.website.model.dto.PostComment;

import com.bhtcnpm.website.model.entity.PostEntities.PostComment;
import com.bhtcnpm.website.repository.PostCommentRepository;
import com.bhtcnpm.website.repository.PostRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Mapping(target = "avatarURL", source = "author.avatarURL")
    @Mapping(target = "childCommentCount", ignore = true)
    public abstract PostCommentDTO postCommentToPostCommentDTOChildCommentOnly (PostComment postComment);

    public PostComment postCommentDTOToPostComment(PostCommentRequestDTO postCommentRequestDTO,
                                                   Long postID, UUID authorID, PostComment entity) {

        PostComment postComment = Objects.requireNonNullElseGet(entity, PostComment::new);

        if (postID == null || postCommentRequestDTO == null || authorID == null) {
            return entity;
        }

        if (postCommentRequestDTO.getParentCommentID() != null) {
            postComment.setParentComment(postCommentRepository.getOne(postCommentRequestDTO.getParentCommentID()));
        } else {
            postComment.setParentComment(null);
        }

        postComment.setPost(postRepository.getOne(postID));
        postComment.setAuthor(userWebsiteRepository.getOne(authorID));
        postComment.setContent(postCommentRequestDTO.getContent());

        return postComment;
    }

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
