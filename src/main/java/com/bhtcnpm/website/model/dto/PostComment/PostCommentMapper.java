package com.bhtcnpm.website.model.dto.PostComment;

import com.bhtcnpm.website.model.entity.PostEntities.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostCommentMapper {
    PostCommentMapper INSTANCE = Mappers.getMapper(PostCommentMapper.class);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "avatarURL", source = "author.avatarURL")
    @Mapping(target = "childCommentCount", ignore = true)
    PostCommentDTO postCommentToPostCommentDTOChildCommentOnly (PostComment postComment);

    List<PostCommentDTO> postCommentListToPostCommentDTOListChildCommentOnly (List<PostComment> postComments);
}
